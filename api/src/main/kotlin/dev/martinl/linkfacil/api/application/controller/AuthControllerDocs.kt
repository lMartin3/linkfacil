package dev.martinl.linkfacil.api.application.controller

import dev.martinl.linkfacil.api.domain.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Authentication", description = "Authentication API")
@RequestMapping("/api/auth")
interface AuthControllerDocs {

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided information. Sends a verification email."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Username or email already taken",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<MessageResponse>

    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = JwtResponse::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Invalid username/email or password",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<Any>

    @Operation(
        summary = "Verify user email",
        description = "Verifies a user's email address using the token sent to their email."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Email verified successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid verification token",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @GetMapping("/verify")
    fun verifyEmail(@RequestParam token: String): ResponseEntity<MessageResponse>

    @Operation(
        summary = "Authenticate user via firebase",
        description = "Authenticates a user via firebase id token and returns a JWT token"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = JwtResponse::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Invalid credential set",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @PostMapping("/signin/firebase")
    fun authenticateUserViaFirebase(loginRequest: FirebaseLoginRequest): ResponseEntity<Any>
}