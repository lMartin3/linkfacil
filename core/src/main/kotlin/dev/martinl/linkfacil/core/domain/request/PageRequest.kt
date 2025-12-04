package dev.martinl.linkfacil.core.domain.request

import dev.martinl.linkfacil.core.domain.PageCode
import dev.martinl.linkfacil.core.domain.exception.ValidationException

data class CreatePageRequest(
    val code: PageCode,
    val name: String,
    val description: String? = null
) {

    init {
        validate()
    }

    private fun validate() {
        if(name.isBlank()) throw ValidationException("The name must not be blank")
        if(description?.isBlank()?:false) throw ValidationException("The description must not be blank if set")
    }
}

data class UpdatePageRequest(
    val title: String?,
    val description: String?,
    val links: List<UpdatePageRequestLink>?,
) {

}

data class UpdatePageRequestLink(
    val id: String?,
    val title: String?,
    val link: String?,
)