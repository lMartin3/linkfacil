package dev.martinl.linkfacil.core.domain.entity

import dev.martinl.linkfacil.core.domain.identifier.PageLinkId
import java.time.Instant

data class PageLink(
    val id : PageLinkId,
    val title: String,
    val link: String,
    val createdAt: Instant,
    val updatedAt: Instant
)