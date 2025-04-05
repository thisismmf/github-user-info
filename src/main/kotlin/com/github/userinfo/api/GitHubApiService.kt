package com.github.userinfo.api

import com.github.userinfo.api.model.Repository
import com.github.userinfo.api.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>
    
    @GET("users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") username: String): Response<List<Repository>>
}