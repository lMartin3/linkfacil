package dev.martinl.linkfacil.api.infrastructure.persistence.config.jwt

import dev.martinl.linkfacil.api.application.service.UserDetailsImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration-ms}")
    private var jwtExpirationMs: Long = 0

    fun generateJwtToken(userDetails: UserDetails): String {
        userDetails as UserDetailsImpl
        val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        return Jwts.builder()
            .setSubject(userDetails.id)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
            .addClaims(mapOf<String, Any>(
                Pair("fullName", userDetails.fullName),
                Pair("email", userDetails.email),
                Pair("permissions", userDetails.authorities.stream().map { it.authority }.toList()),
            ))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String): String {
        val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body


        return claims.subject
    }

    fun getUserDetailsFromJwtToken(token: String): UserDetails {
        val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body


        return UserDetailsImpl(
            claims.subject,
            claims["email"] as String,
            claims["fullName"] as String,
            claims["email"] as String,
            "",
            (claims["permissions"] as List<*>).stream().map { SimpleGrantedAuthority(it.toString()) }.toList()
        )
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)

            return true
        } catch (e: Exception) {
            // Log the exception
            return false
        }
    }
}
