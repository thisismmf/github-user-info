package com.github.userinfo

import com.github.userinfo.api.GitHubApiService
import com.github.userinfo.cache.UserCache
import com.github.userinfo.ui.Menu
import com.github.userinfo.util.ErrorHandler
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun main() {
    // Configure OkHttp client
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    
    val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Configure Gson
    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .setLenient()
        .create()
    
    // Create Retrofit instance
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    // Create API service
    val apiService = retrofit.create(GitHubApiService::class.java)
    
    // Initialize cache and error handler
    val userCache = UserCache()
    val errorHandler = ErrorHandler()
    
    // Start the menu
    val menu = Menu(apiService, userCache, errorHandler)
    menu.start()
}