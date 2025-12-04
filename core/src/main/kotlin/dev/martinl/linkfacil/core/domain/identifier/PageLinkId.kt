package dev.martinl.linkfacil.core.domain.identifier

@JvmInline
value class PageLinkId(override val value: String) : Identifier {
    init {
        validate()
    }

    companion object {
        fun generate(): PageLinkId {
            return PageLinkId(Identifier.next())
        }
    }
}
