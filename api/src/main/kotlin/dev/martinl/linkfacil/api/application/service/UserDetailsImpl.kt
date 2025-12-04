package dev.martinl.linkfacil.api.application.service

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.martinl.linkfacil.core.domain.entity.User
import dev.martinl.linkfacil.core.domain.entity.UserProfile
import dev.martinl.linkfacil.core.domain.identifier.UserId
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    val id: String,
    private val username: String,
    override val fullName: String,
    override val email: String,
    @JsonIgnore
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails, UserProfile {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String? = password
    override fun getUsername(): String? {
        return username
    }

    override fun getId(): UserId {
        return UserId(id)
    }

    override fun getProfilePicture(): String {
        return ""
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

            return UserDetailsImpl(
                id = user.id.value,
                username = user.email,
                fullName = user.fullName,
                email = user.email,
                password = user.hashedPassword,
                authorities = authorities
            )
        }
    }
}