package dev.martinl.linkfacil.api.application.controller

import dev.martinl.linkfacil.api.application.service.AuthService
import dev.martinl.linkfacil.api.domain.dto.FirebaseLoginRequest
import dev.martinl.linkfacil.api.domain.dto.LoginRequest
import dev.martinl.linkfacil.api.domain.dto.MessageResponse
import dev.martinl.linkfacil.api.domain.dto.SignupRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService, private val authenticationManager: AuthenticationManager) : AuthControllerDocs {

    override fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<MessageResponse> {
        val isRegistered = authService.registerUser(signupRequest)

        return if (isRegistered) {
            ResponseEntity.ok(MessageResponse("User registered successfully! Please check your email to verify your account."))
        } else {
            ResponseEntity.badRequest().body(MessageResponse("Error: Username or email is already taken!"))
        }
    }

    override fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<Any> = runCatching {

        authService.authenticateUser(loginRequest)
    }.fold(
        onSuccess = {
            ResponseEntity.ok(it)
        },
        onFailure = {
            when(it) {
                is BadCredentialsException ->  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MessageResponse(it.message ?: "Incorrect credentials"))
                else -> {
                     ResponseEntity.internalServerError().body(it.message)
                }
            }
        }
    )

    override fun authenticateUserViaFirebase(@RequestBody loginRequest: FirebaseLoginRequest): ResponseEntity<Any> = runCatching {
        authService.authenticateUserWithFirebase(loginRequest)
    }.fold(
        onSuccess = {
            ResponseEntity.ok(it)
        },
        onFailure = {
            when(it) {
                is BadCredentialsException ->  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(MessageResponse(it.message ?: "Incorrect credentials"))
                else -> {
                    ResponseEntity.internalServerError().body(it.message)
                }
            }
        }
    )


    override fun verifyEmail(@RequestParam token: String): ResponseEntity<MessageResponse> {
        val isVerified = authService.verifyEmail(token)

        return if (isVerified) {
            ResponseEntity.ok(MessageResponse("Email verified successfully!"))
        } else {
            ResponseEntity.badRequest().body(MessageResponse("Error: Invalid verification token!"))
        }
    }
}
