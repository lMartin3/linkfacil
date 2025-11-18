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
    
    @Indexed(unique = true)
    val username: String,
    
    @Indexed(unique = true)
    val email: String,
    
    val hashedPassword: String,
    
    val profilePicture: String?,
    
    val emailVerified: Boolean = false,
    
    val verificationToken: String? = null
) {
    fun toDomain(): User {
        return User(
            id = UserId(id),
            username = username,
            email = email,
            hashedPassword = hashedPassword,
            profilePicture = profilePicture,
            emailVerified = emailVerified,
            verificationToken = verificationToken
        )
    }

    companion object {
        fun fromDomain(user: User): MongoUser {
            return MongoUser(
                id = user.id.value,
                username = user.username,
                email = user.email,
                hashedPassword = user.hashedPassword,
                profilePicture = user.profilePicture,
                emailVerified = user.emailVerified,
                verificationToken = user.verificationToken
            )
        }
    }
}