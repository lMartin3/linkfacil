package dev.martinl.linkfacil.core.domain.repository

import dev.martinl.linkfacil.core.domain.entity.User
import dev.martinl.linkfacil.core.domain.identifier.UserId

interface UserRepository : BaseRepository<User, UserId> {
}
