package dev.martinl.linkfacil.api.infrastructure.persistence.entity

import dev.martinl.linkfacil.core.domain.entity.PageLink
import dev.martinl.linkfacil.core.domain.identifier.PageLinkId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "users")
data class MongoPageLink(
    @Id
    val id: String,

    val title: String,

    val link: String,

    val createdAt: Instant,
    val updatedAt: Instant
) {
    fun toDomain(): PageLink {
        return PageLink(
            PageLinkId(id),
            title,
            link,
            createdAt,
            updatedAt
        )
    }

    companion object {
        fun fromDomain(page: PageLink): MongoPageLink {
            return MongoPageLink(
                id = page.id.value,
                title = page.title,
                link = page.link,
                createdAt = page.createdAt,
                updatedAt = page.updatedAt,
            )
        }
    }
}