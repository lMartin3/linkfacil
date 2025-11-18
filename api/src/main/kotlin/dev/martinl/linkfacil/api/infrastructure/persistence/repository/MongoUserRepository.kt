package dev.martinl.linkfacil.api.infrastructure.persistence.repository

import dev.martinl.linkfacil.api.infrastructure.persistence.entity.MongoUser
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoUserRepository : MongoRepository<MongoUser, String> {
    fun findByUsername(username: String): MongoUser?
    fun findByEmail(email: String): MongoUser?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}