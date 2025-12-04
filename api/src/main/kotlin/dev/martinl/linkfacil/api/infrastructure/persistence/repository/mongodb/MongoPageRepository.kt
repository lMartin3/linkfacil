package dev.martinl.linkfacil.api.infrastructure.persistence.repository.mongodb

import dev.martinl.linkfacil.api.infrastructure.persistence.entity.MongoPage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoPageRepository : MongoRepository<MongoPage, String> {
    fun findByCode(code: String): MongoPage?
    fun findByOwnerId(ownerId: String): List<MongoPage>

}