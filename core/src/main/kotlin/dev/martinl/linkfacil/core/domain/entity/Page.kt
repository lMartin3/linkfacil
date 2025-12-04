package dev.martinl.linkfacil.core.domain.entity

import dev.martinl.linkfacil.core.domain.PageCode
import dev.martinl.linkfacil.core.domain.identifier.PageId
import dev.martinl.linkfacil.core.domain.identifier.UserId
import java.time.Instant

data class Page(
    val id: PageId,
    val code: PageCode,
    val owner: UserId,
    val title: String,
    val description: String?,
    val links: List<PageLink>,
    val createdAt: Instant,
    val updatedAt: Instant,
) {

}