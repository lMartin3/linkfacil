package dev.martinl.linkfacil.core.domain.repository

import dev.martinl.linkfacil.core.domain.entity.Page
import dev.martinl.linkfacil.core.domain.identifier.PageId

interface PageRepository : BaseRepository<Page, PageId> {
    fun findByCode(code: String): Page?
    fun findByOwnerId(ownerId: String): List<Page>
}
