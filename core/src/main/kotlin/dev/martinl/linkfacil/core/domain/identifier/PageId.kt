package dev.martinl.linkfacil.core.domain.identifier

@JvmInline
value class PageId(override val value: String) : Identifier {
    init {
        validate()
    }

    companion object {
        fun generate(): PageId {
            return PageId(Identifier.next())
        }
    }
}
