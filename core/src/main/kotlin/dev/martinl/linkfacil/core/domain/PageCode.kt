package dev.martinl.linkfacil.core.domain

import dev.martinl.linkfacil.core.domain.identifier.InvalidIdentifierException

@JvmInline
value class PageCode(
    val code: String
) {

    init {
        validate()
    }

    fun validate() {
        if(code.isEmpty() || code.isBlank()) throw InvalidIdentifierException("Invalid page code: $code")
    }
}