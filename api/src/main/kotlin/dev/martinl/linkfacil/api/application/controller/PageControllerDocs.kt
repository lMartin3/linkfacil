package dev.martinl.linkfacil.api.application.controller

import dev.martinl.linkfacil.api.domain.dto.MessageResponse
import dev.martinl.linkfacil.core.domain.request.CreatePageRequest
import dev.martinl.linkfacil.core.domain.request.UpdatePageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Page", description = "Page API")
@RequestMapping("/api/page")
interface PageControllerDocs {

    @Operation(
        summary = "Gets the user's pages",
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Pages found successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
    )
    @GetMapping("")
    fun getUserPages(): ResponseEntity<Any>

    @Operation(
        summary = "Find a page by id",
        description = "Finds a page by id"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Page found successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Page not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @GetMapping("/{pageId}")
    fun getPageById(@PathVariable pageId : String): ResponseEntity<Any>

    @Operation(
        summary = "Find a page by code",
        description = "Finds a page by code"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Page found successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Page not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @GetMapping("/code/{pageCode}")
    fun getPageByCode(@PathVariable pageCode : String): ResponseEntity<Any>

    @Operation(
        summary = "Create a new page",
        description = "Creates a new page with the provided information"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Page created successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Error creating page",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @PostMapping()
    fun createPage(@RequestBody signupRequest: CreatePageRequest): ResponseEntity<Any>

    @Operation(
        summary = "Update a page by id",
        description = "Updates a page by id with the provided information"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Page found successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Request error",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Page not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @PatchMapping("/{pageId}")
    fun updatePage(@PathVariable pageId : String, @RequestBody updatePageRequest: UpdatePageRequest): ResponseEntity<MessageResponse>

    @Operation(
        summary = "Deletes a page by id",
        description = "Deletes a user's page by id"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Page deleted successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Request error",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "403",
            description = "Insufficient permissions",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Page not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = MessageResponse::class)
            )]
        )
    )
    @DeleteMapping("/{pageId}")
    fun deletePage(@PathVariable("pageId") pageId: String): ResponseEntity<MessageResponse>
}