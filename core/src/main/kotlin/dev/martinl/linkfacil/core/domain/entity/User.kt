package dev.martinl.linkfacil.core.domain.entity

import dev.martinl.linkfacil.core.domain.identifier.UserId

data class User(
    val id: UserId,
    val fullName: String,
    val email: String,
    val hashedPassword: String?,
    val profilePicture: String? = null,
    val emailVerified: Boolean = false,
    val verificationToken: String? = null,
    val permissions: List<String>
) {
}
