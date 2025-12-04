package dev.martinl.linkfacil.api.infrastructure.persistence.repository

import dev.martinl.linkfacil.api.infrastructure.persistence.entity.MongoUser
import dev.martinl.linkfacil.api.infrastructure.persistence.repository.mongodb.MongoUserRepository
import dev.martinl.linkfacil.core.domain.entity.User
import dev.martinl.linkfacil.core.domain.identifier.UserId
import dev.martinl.linkfacil.core.domain.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(private val mongoUserRepository: MongoUserRepository) : UserRepository {
    
    override fun findAll(): List<User> {
        return mongoUserRepository.findAll().map { it.toDomain() }
    }
    
    override fun findById(id: UserId): User? {
        return mongoUserRepository.findById(id.value).orElse(null)?.toDomain()
    }

    override fun deleteById(id: UserId) {
        return mongoUserRepository.deleteById(id.value)
    }
    
    fun findByEmail(email: String): User? {
        return mongoUserRepository.findByEmail(email)?.toDomain()
    }
    
    fun existsByEmail(email: String): Boolean {
        return mongoUserRepository.existsByEmail(email)
    }
    
    override fun save(user: User): User {
        val mongoUser = MongoUser.fromDomain(user)
        return mongoUserRepository.save(mongoUser).toDomain()
    }
    
    fun deleteById(id: String) {
        mongoUserRepository.deleteById(id)
    }
}