package dev.martinl.linkfacil.api.infrastructure.persistence.repository

import dev.martinl.linkfacil.api.infrastructure.persistence.entity.MongoPage
import dev.martinl.linkfacil.api.infrastructure.persistence.repository.mongodb.MongoPageRepository
import dev.martinl.linkfacil.core.domain.entity.Page
import dev.martinl.linkfacil.core.domain.identifier.PageId
import dev.martinl.linkfacil.core.domain.repository.PageRepository
import org.springframework.stereotype.Repository

@Repository
class PageRepositoryImpl(
    private val mongoPageRepository: MongoPageRepository
) : PageRepository {
    override fun findAll(): List<Page> {
        return mongoPageRepository.findAll().map { it.toDomain() }
    }

    override fun findById(id: PageId): Page? {
        return mongoPageRepository.findById(id.value).orElse(null)?.toDomain()
    }

    override fun findByCode(code: String): Page? {
        return mongoPageRepository.findByCode(code)?.toDomain()
    }

    override fun findByOwnerId(ownerId: String): List<Page> {
       return mongoPageRepository.findByOwnerId(ownerId).map { it.toDomain() }.toList()
    }

    override fun deleteById(id: PageId) {
        mongoPageRepository.deleteById(id.value)
    }

    override fun save(entity: Page): Page {
        return mongoPageRepository.save(MongoPage.fromDomain(entity)).toDomain()
    }
}