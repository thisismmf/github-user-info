package com.github.userinfo.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url") val avatarUrl: String,
    val name: String?,
    val bio: String?,
    val followers: Int,
    val following: Int,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("created_at") val createdAt: String,
    var repositories: List<Repository> = emptyList()
) {
    fun getFormattedCreationDate(): String {
        return createdAt.substring(0, 10) // Simplified for display as YYYY-MM-DD
    }
    
    override fun toString(): String {
        return """
            Username: $login
            Name: ${name ?: "N/A"}
            Bio: ${bio ?: "N/A"}
            Followers: $followers
            Following: $following
            Public Repositories: $publicRepos
            Account created on: ${getFormattedCreationDate()}
        """.trimIndent()
    }
}