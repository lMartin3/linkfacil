package dev.martinl.linkfacil.api.application.controller

import dev.martinl.linkfacil.api.domain.dto.MessageResponse
import dev.martinl.linkfacil.core.application.PageService
import dev.martinl.linkfacil.core.domain.identifier.PageId
import dev.martinl.linkfacil.core.domain.request.CreatePageRequest
import dev.martinl.linkfacil.core.domain.request.UpdatePageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PageController(
    private val pageService: PageService
) : PageControllerDocs {

    override fun getUserPages(): ResponseEntity<Any> {
        return ResponseEntity.ok(pageService.getUserPages())
    }

    override fun getPageById(pageId: String): ResponseEntity<Any> {
        return ResponseEntity.ok(pageService.getPageById(PageId(pageId)))
    }

    override fun getPageByCode(pageCode: String): ResponseEntity<Any> {
        return ResponseEntity.ok(pageService.getPageByCode(pageCode))
    }

    override fun createPage(createPageRequest: CreatePageRequest): ResponseEntity<Any> = runCatching {
        pageService.createPage(createPageRequest)
    }.fold(
        onSuccess = { ResponseEntity.ok(it) },
        onFailure = { ResponseEntity.badRequest().body(MessageResponse("Error: ${it.message}")) }
    )

    override fun updatePage(
        pageId: String,
        updatePageRequest: UpdatePageRequest
    ): ResponseEntity<MessageResponse> {
        pageService.updatePage(PageId(pageId), updatePageRequest)
        return ResponseEntity.ok(MessageResponse("Page updated successfully!"))
    }

    override fun deletePage(
        pageId: String
    ): ResponseEntity<MessageResponse> {
        pageService.deletePage(PageId(pageId))
        return ResponseEntity.ok(MessageResponse("Page deleted"))
    }
}