package dev.martinl.linkfacil.api.infrastructure.persistence.config.service

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.martinl.linkfacil.core.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    val id: String,
    private val username: String,
    val email: String,
    @JsonIgnore
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

            return UserDetailsImpl(
                id = user.id.value,
                username = user.username,
                email = user.email,
                password = user.hashedPassword,
                authorities = authorities
            )
        }
    }
}