package dev.martinl.linkfacil.api.infrastructure.config

import com.google.firebase.auth.FirebaseToken
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.function.Consumer


class FirebaseAuthenticationToken(private val firebaseToken: FirebaseToken) : AbstractAuthenticationToken(
    extractAuthorities(
        firebaseToken
    )
) {
    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any {
        return firebaseToken
    }

    override fun getPrincipal(): Any? {
        return firebaseToken.uid
    }

    companion object {
        private fun extractAuthorities(token: FirebaseToken): MutableCollection<out GrantedAuthority?> {
            val authorities: MutableList<GrantedAuthority?> = ArrayList<GrantedAuthority?>()

            if (token.claims["role"] != null) {
                authorities.add(SimpleGrantedAuthority("ROLE_" + token.claims["role"]))
            }

            if (token.claims["permissions"] != null) {
                val perms = token.claims["permissions"] as MutableList<String?>
                perms.forEach(Consumer { p: String? -> authorities.add(SimpleGrantedAuthority(p)) })
            }

            return authorities
        }
    }
}