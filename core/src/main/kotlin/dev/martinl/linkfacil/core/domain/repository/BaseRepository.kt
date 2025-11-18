package dev.martinl.linkfacil.core.domain.repository

interface BaseRepository<T, I> {
    fun findAll() : List<T>
    fun findById(id: I) : T?
    fun deleteById(id: I)
    fun save(entity: T) : T
}