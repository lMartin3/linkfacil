package dev.martinl.linkfacil.core.application

import dev.martinl.linkfacil.core.domain.entity.UserProfile
import dev.martinl.linkfacil.core.domain.identifier.UserId

interface UserContextProvider {
    fun getCurrentUserId(): UserId
    fun getCurrentUserProfile() : UserProfile
}