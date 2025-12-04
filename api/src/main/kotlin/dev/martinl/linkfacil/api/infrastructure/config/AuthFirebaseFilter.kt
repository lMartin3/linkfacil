package dev.martinl.linkfacil.api.infrastructure.persistence.config.jwt

import com.google.firebase.auth.FirebaseAuth
import dev.martinl.linkfacil.api.application.service.UserDetailsImpl
import dev.martinl.linkfacil.api.infrastructure.persistence.config.service.UserDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter


//@Component
class AuthFirebaseFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = parseJwt(request)
            val decoded = FirebaseAuth.getInstance().verifyIdToken(jwt)
            val role = decoded.claims["role"] as String?
            val permissions = decoded.claims["permissions"] as MutableList<String?>?

            val authorities = permissions?.map { SimpleGrantedAuthority(it) }
            val authentication = UsernamePasswordAuthenticationToken(
                UserDetailsImpl(
                    decoded.uid,
                    decoded.email,
                    decoded.name,
                    decoded.email,
                    "",
                    listOf()

                ), null, authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication

        } catch (e: Exception) {
            logger.error("Cannot set user authentication: ${e.message}")
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7)
        } else null
    }
}