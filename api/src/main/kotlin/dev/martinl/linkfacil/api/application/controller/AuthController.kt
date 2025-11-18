package dev.martinl.linkfacil.api.application.controller

import dev.martinl.linkfacil.api.domain.dto.JwtResponse
import dev.martinl.linkfacil.api.domain.dto.LoginRequest
import dev.martinl.linkfacil.api.domain.dto.MessageResponse
import dev.martinl.linkfacil.api.domain.dto.SignupRequest
import dev.martinl.linkfacil.api.application.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
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

    override fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<JwtResponse> {
        println("Provider list: " + (authenticationManager as ProviderManager).providers)
        val jwtResponse = authService.authenticateUser(loginRequest)
        return ResponseEntity.ok(jwtResponse)
    }

    override fun verifyEmail(@RequestParam token: String): ResponseEntity<MessageResponse> {
        val isVerified = authService.verifyEmail(token)

        return if (isVerified) {
            ResponseEntity.ok(MessageResponse("Email verified successfully!"))
        } else {
            ResponseEntity.badRequest().body(MessageResponse("Error: Invalid verification token!"))
        }
    }
}
