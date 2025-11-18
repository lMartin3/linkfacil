package dev.martinl.linkfacil.api.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    val username: String,
    
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    val password: String,
    
    val profilePicture: String? = null
)