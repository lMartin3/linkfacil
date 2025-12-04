package dev.martinl.linkfacil.api.application.service

import com.google.firebase.auth.FirebaseAuth
import dev.martinl.linkfacil.api.domain.dto.FirebaseLoginRequest
import dev.martinl.linkfacil.api.domain.dto.JwtResponse
import dev.martinl.linkfacil.api.domain.dto.LoginRequest
import dev.martinl.linkfacil.api.domain.dto.SignupRequest
import dev.martinl.linkfacil.api.infrastructure.persistence.config.jwt.JwtUtils
import dev.martinl.linkfacil.api.infrastructure.persistence.repository.UserRepositoryImpl
import dev.martinl.linkfacil.core.application.UserContextProvider
import dev.martinl.linkfacil.core.domain.entity.User
import dev.martinl.linkfacil.core.domain.entity.UserProfile
import dev.martinl.linkfacil.core.domain.exception.UnauthorizedAccessException
import dev.martinl.linkfacil.core.domain.identifier.UserId
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
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
) : UserContextProvider {

    fun registerUser(signupRequest: SignupRequest): Boolean {
        if (userRepository.existsByEmail(signupRequest.email)) {
            return false
        }

        val verificationToken = UUID.randomUUID().toString()

        val user = User(
            id = UserId.generate(),
            fullName = signupRequest.username,
            email = signupRequest.email,
            hashedPassword = passwordEncoder.encode(signupRequest.password),
            profilePicture = signupRequest.profilePicture,
            emailVerified = false,
            verificationToken = verificationToken,
            permissions = listOf()
        )

        userRepository.save(user)

        emailService.sendVerificationEmail(user.email, verificationToken)

        return true
    }

    fun authenticateUserWithFirebase(loginRequest: FirebaseLoginRequest): JwtResponse {
        val idToken = loginRequest.idToken

        val decoded = FirebaseAuth.getInstance().verifyIdToken(idToken)

        val email = decoded.email ?: throw BadCredentialsException("No email specified in Firebase token")
        val name = decoded.name ?: throw BadCredentialsException("No name specified in Firebase token")
        val picture = decoded.picture
        val uid = decoded.uid

        // Look up or create a local user
        val user : User = userRepository.findByEmail(email) ?: userRepository.save(User(
                UserId.generate(),
                name,
                email,
                null,
                picture,
                true,
                null,
            listOf()
            ))


        val jwt = jwtUtils.generateJwtToken(UserDetailsImpl(
            user.id.value,
            user.email,
            user.fullName,
            user.email,
            user.hashedPassword,
            user.permissions.map { SimpleGrantedAuthority(it) }
        ))

        return JwtResponse(
            token = jwt,
            id = user.id.value,
            username = user.fullName,
            email = user.email
        )
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

        val userDetails = authentication.principal as UserDetailsImpl

        val jwt = jwtUtils.generateJwtToken(userDetails)

        val user = userRepository.findByEmail(userDetails.email)
            ?: throw RuntimeException("User not found")

        return JwtResponse(
            token = jwt,
            id = user.id.value,
            username = user.fullName,
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
    override fun getCurrentUserId(): UserId {
        return UserId(getCurrentUser().id)
    }


    fun getCurrentUser(): UserDetailsImpl {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        val userDetails = principal as? UserDetailsImpl ?: throw UnauthorizedAccessException("No authenticated user found or user details not available")
        return userDetails
    }

    override fun getCurrentUserProfile(): UserProfile {
        return getCurrentUser()
    }
}
