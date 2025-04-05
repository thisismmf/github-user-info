package com.github.userinfo.api.model

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    val description: String?,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("stargazers_count") val stars: Int,
    val language: String?
) {
    override fun toString(): String {
        return """
            Name: $name
            Description: ${description ?: "N/A"}
            Language: ${language ?: "N/A"}
            Stars: $stars
            URL: $htmlUrl
        """.trimIndent()
    }
}