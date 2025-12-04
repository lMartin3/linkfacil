package dev.martinl.linkfacil.api.infrastructure.persistence.entity

import dev.martinl.linkfacil.core.domain.entity.User
import dev.martinl.linkfacil.core.domain.identifier.UserId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class MongoUser(
    @Id
    val id: String,

    val fullName: String,
    
    @Indexed(unique = true)
    val email: String,
    
    val hashedPassword: String?,
    
    val profilePicture: String?,
    
    val emailVerified: Boolean = false,
    
    val verificationToken: String? = null,

    val permissions: List<String> = listOf()
) {
    fun toDomain(): User {
        return User(
            id = UserId(id),
            fullName = fullName,
            email = email,
            hashedPassword = hashedPassword,
            profilePicture = profilePicture,
            emailVerified = emailVerified,
            verificationToken = verificationToken,
            permissions = permissions
        )
    }

    companion object {
        fun fromDomain(user: User): MongoUser {
            return MongoUser(
                id = user.id.value,
                fullName = user.fullName,
                email = user.email,
                hashedPassword = user.hashedPassword,
                profilePicture = user.profilePicture,
                emailVerified = user.emailVerified,
                verificationToken = user.verificationToken,
                permissions = user.permissions

            )
        }
    }
}