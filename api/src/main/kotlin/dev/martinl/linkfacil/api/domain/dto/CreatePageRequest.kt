package dev.martinl.linkfacil.api.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreatePageRequestttt(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    val name: String,

    @field:Size(min = 3, message = "The description must have at least 3 characters")
    val description: String?,
)