package dev.martinl.linkfacil.api.domain.dto

data class JwtResponse(
    val token: String,
    val type: String = "Bearer",
    val id: String,
    val username: String,
    val email: String
)