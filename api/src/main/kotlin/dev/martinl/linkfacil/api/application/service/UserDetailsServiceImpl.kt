package dev.martinl.linkfacil.api.infrastructure.persistence.config.service

import dev.martinl.linkfacil.api.infrastructure.persistence.repository.UserRepositoryImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepositoryImpl) : UserDetailsService {
    
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found with username or email: $username")
            
        return UserDetailsImpl.build(user)
    }
}