package dev.martinl.linkfacil.api.infrastructure.persistence.repository.mongodb

import dev.martinl.linkfacil.api.infrastructure.persistence.entity.MongoUser
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoUserRepository : MongoRepository<MongoUser, String> {
    fun findByEmail(email: String): MongoUser?
    fun existsByEmail(email: String): Boolean
}