package dev.martinl.linkfacil.api.infrastructure.persistence.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
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
        return generateTokenFromUsername(userDetails.username)
    }

    fun generateTokenFromUsername(username: String): String {
        val key: SecretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
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
