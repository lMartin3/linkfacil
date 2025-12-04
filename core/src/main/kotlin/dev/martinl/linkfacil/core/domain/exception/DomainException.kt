package dev.martinl.linkfacil.core.domain.exception

open class DomainException(message: String) : RuntimeException(message)

class ValidationException(message: String) : DomainException(message)
class NotFoundException(message: String) : DomainException(message)
class UnauthorizedAccessException(message: String) : DomainException(message)
