package dev.martinl.linkfacil.api.infrastructure.config

import dev.martinl.linkfacil.core.application.PageService
import dev.martinl.linkfacil.core.application.UserContextProvider
import dev.martinl.linkfacil.core.domain.repository.PageRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DependencyInjection(
    private val pageRepository: PageRepository,
    private val userContextProvider: UserContextProvider
) {

    @Bean
    fun pageService() : PageService {
        return PageService(pageRepository, userContextProvider)
    }
}