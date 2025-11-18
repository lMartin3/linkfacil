package dev.martinl.linkfacil.core.domain.identifier

import java.util.*

interface Identifier {
    abstract val value: String

    fun validate() {
        try {
            checkNotNull(UUID.fromString(value))
        } catch (_: Exception) {
            throw InvalidIdentifierException("Invalid identifier: $value")
        }
    }

    companion object {
        fun next(): String {
            return UUID.randomUUID().toString();
        }
    }
}

class InvalidIdentifierException(message: String) : RuntimeException(message)
