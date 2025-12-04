package dev.martinl.linkfacil.core.domain.entity

import dev.martinl.linkfacil.core.domain.identifier.UserId

interface UserProfile {

    val fullName : String
    val email : String
    fun getId(): UserId
    fun getProfilePicture(): String
}