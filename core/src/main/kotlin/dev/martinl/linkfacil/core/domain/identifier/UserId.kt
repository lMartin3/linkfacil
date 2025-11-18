package dev.martinl.linkfacil.core.domain.identifier

@JvmInline
value class UserId(override val value: String) : Identifier {
    init {
        validate()
    }

    companion object {
        fun generate(): UserId {
            return UserId(Identifier.next())
        }
    }
}
