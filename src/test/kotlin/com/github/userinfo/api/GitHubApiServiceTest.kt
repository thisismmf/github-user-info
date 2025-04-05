package com.github.userinfo.api

import com.github.userinfo.api.model.User
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GitHubApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: GitHubApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setLenient()
            .create()
            
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            
        apiService = retrofit.create(GitHubApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test get user`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("""
                {
                    "login": "testuser",
                    "id": 123,
                    "avatar_url": "https://example.com/avatar.jpg",
                    "name": "Test User",
                    "bio": "Test bio",
                    "followers": 100,
                    "following": 50,
                    "public_repos": 10,
                    "created_at": "2020-01-01T00:00:00Z"
                }
            """.trimIndent())
            
        mockWebServer.enqueue(mockResponse)
        
        val response = apiService.getUser("testuser")
        val user = response.body()
        
        assertEquals(true, response.isSuccessful)
        assertEquals("testuser", user?.login)
        assertEquals(100, user?.followers)
        assertEquals("2020-01-01T00:00:00Z", user?.createdAt)
    }
}