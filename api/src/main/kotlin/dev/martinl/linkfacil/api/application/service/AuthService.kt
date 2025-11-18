package dev.martinl.linkfacil.api.application.service

import dev.martinl.linkfacil.api.domain.dto.JwtResponse
import dev.martinl.linkfacil.api.domain.dto.LoginRequest
import dev.martinl.linkfacil.api.domain.dto.SignupRequest
import dev.martinl.linkfacil.api.infrastructure.persistence.repository.UserRepositoryImpl
import dev.martinl.linkfacil.api.infrastructure.persistence.config.jwt.JwtUtils
import dev.martinl.linkfacil.api.infrastructure.persistence.config.service.UserDetailsImpl
import dev.martinl.linkfacil.core.domain.entity.User
import dev.martinl.linkfacil.core.domain.identifier.UserId
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepositoryImpl,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val authenticationManager: AuthenticationManager,
    private val emailService: EmailService
) {

    fun registerUser(signupRequest: SignupRequest): Boolean {
        // Check if username is already taken
        if (userRepository.existsByUsername(signupRequest.username)) {
            return false
        }

        // Check if email is already in use
        if (userRepository.existsByEmail(signupRequest.email)) {
            return false
        }

        // Create verification token
        val verificationToken = UUID.randomUUID().toString()

        // Create new user
        val user = User(
            id = UserId.generate(),
            username = signupRequest.username,
            email = signupRequest.email,
            hashedPassword = passwordEncoder.encode(signupRequest.password),
            profilePicture = signupRequest.profilePicture,
            emailVerified = false,
            verificationToken = verificationToken
        )

        // Save user
        userRepository.save(user)

        // Send verification email
        emailService.sendVerificationEmail(user.email, verificationToken)

        return true
    }

    fun authenticateUser(loginRequest: LoginRequest): JwtResponse {
        val authentication = runCatching {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.usernameOrEmail, loginRequest.password)
            )
        }.fold({
            it
        }, {
            throw it
        })

        // Set authentication in security context
        // SecurityContextHolder.getContext().authentication = authentication
        val userDetails = authentication.principal as UserDetailsImpl

        // Generate JWT token
        val jwt = jwtUtils.generateJwtToken(userDetails)

        // Find user by username from userDetails
        val user = userRepository.findByUsername(userDetails.username) 
            ?: userRepository.findByEmail(userDetails.username) 
            ?: throw RuntimeException("User not found")

        return JwtResponse(
            token = jwt,
            id = user.id.value,
            username = user.username,
            email = user.email
        )
    }

    fun verifyEmail(token: String): Boolean {
        val users = userRepository.findAll()
        val user = users.find { it.verificationToken == token } ?: return false

        val updatedUser = user.copy(emailVerified = true, verificationToken = null)
        userRepository.save(updatedUser)
        return true
    }
}
