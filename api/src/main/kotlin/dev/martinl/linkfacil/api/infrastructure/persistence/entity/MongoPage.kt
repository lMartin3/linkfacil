package dev.martinl.linkfacil.api.infrastructure.persistence.entity

import dev.martinl.linkfacil.core.domain.PageCode
import dev.martinl.linkfacil.core.domain.entity.Page
import dev.martinl.linkfacil.core.domain.identifier.PageId
import dev.martinl.linkfacil.core.domain.identifier.UserId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "pages")
data class MongoPage(
    @Id
    val id: String,

    @Indexed(unique = true)
    val code: String,

    val ownerId: String,

    val title: String,

    val description: String?,

    val links: List<MongoPageLink>,

    val createdAt: Instant,
    val updatedAt: Instant
) {
    fun toDomain(): Page {
        return Page(
            PageId(id),
            PageCode(code),
            UserId(ownerId),
            title,
            description,
            links.map { it.toDomain() },
            createdAt,
            updatedAt
        )
    }

    companion object {
        fun fromDomain(page: Page): MongoPage {
            return MongoPage(
                id = page.id.value,
                code = page.code.code,
                ownerId = page.owner.value,
                title = page.title,
                description = page.description,
                links = page.links.map { MongoPageLink.fromDomain(it) },
                createdAt = page.createdAt,
                updatedAt = page.updatedAt,
            )
        }
    }
}