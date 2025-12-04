package dev.martinl.linkfacil.core.application

import dev.martinl.linkfacil.core.domain.entity.Page
import dev.martinl.linkfacil.core.domain.entity.PageLink
import dev.martinl.linkfacil.core.domain.exception.NotFoundException
import dev.martinl.linkfacil.core.domain.exception.UnauthorizedAccessException
import dev.martinl.linkfacil.core.domain.identifier.PageId
import dev.martinl.linkfacil.core.domain.identifier.PageLinkId
import dev.martinl.linkfacil.core.domain.repository.PageRepository
import dev.martinl.linkfacil.core.domain.request.CreatePageRequest
import dev.martinl.linkfacil.core.domain.request.UpdatePageRequest
import java.time.Instant

class PageService(private val pageRepository: PageRepository, private val userContextProvider: UserContextProvider) {

    fun getUserPages() : List<Page> {
        return pageRepository.findByOwnerId(userContextProvider.getCurrentUserId().value)
    }

    fun getPageById(id: PageId) : Page {
        return pageRepository.findById(id) ?: throw NotFoundException("Page with id $id not found")
    }

    fun getPageByCode(code: String) : Page {
        return pageRepository.findByCode(code) ?: throw NotFoundException("Page with code $code not found")
    }

    fun createPage(request : CreatePageRequest) : Page {
        return pageRepository.save(Page(
            PageId.generate(),
            request.code,
            userContextProvider.getCurrentUserId(),
            request.name,
            request.description,
            emptyList(),
            Instant.now(),
            Instant.now()
        ));
    }

    fun updatePage(id: PageId, request: UpdatePageRequest) {
        val page = pageRepository.findById(id) ?: throw NotFoundException("Page with id $id not found")
        var description = request.description
        if(description==null) {
            description = page.description
        } else if(description.isBlank()) {
            description = null
        }

        val currentUserId = userContextProvider.getCurrentUserId()
        if(currentUserId!=page.owner) throw UnauthorizedAccessException("You are not allowed to edit this page")

        val mappedLinks : List<PageLink>? = request.links?.map { reqLink ->
            PageLink(
                reqLink.id?.let { id->PageLinkId(id) }?: PageLinkId.generate(),
                reqLink.title ?: "",
                reqLink.link ?: "",
                Instant.now(),
                Instant.now()
            )
        }?.toList() ?: null

        val updatedPage = page.copy(
            title = request.title ?: page.title,
            description = description,
            links = mappedLinks ?: page.links,
            updatedAt = Instant.now()
        )
        pageRepository.save(updatedPage)
    }

    fun deletePage(id: PageId) {
        val page = pageRepository.findById(id) ?: throw NotFoundException("Page with id $id not found")

        val currentUserId = userContextProvider.getCurrentUserId()
        if(currentUserId!=page.owner) throw UnauthorizedAccessException("You are not allowed to delete this page")


        pageRepository.deleteById(id)
    }
}