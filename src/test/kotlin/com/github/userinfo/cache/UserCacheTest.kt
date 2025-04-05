package com.github.userinfo.cache

import com.github.userinfo.api.model.Repository
import com.github.userinfo.api.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserCacheTest {
    private lateinit var userCache: UserCache
    
    @Before
    fun setup() {
        userCache = UserCache()
    }
    
    @Test
    fun `test add and retrieve user`() {
        val testUser = User(
            login = "testuser",
            id = 123,
            avatarUrl = "https://example.com/avatar.jpg",
            name = "Test User",
            bio = "Test bio",
            followers = 100,
            following = 50,
            publicRepos = 10,
            createdAt = "2020-01-01T00:00:00Z"
        )
        
        userCache.addUser(testUser)
        
        val retrievedUser = userCache.getUser("testuser")
        assertNotNull(retrievedUser)
        assertEquals(testUser.login, retrievedUser?.login)
    }
    
    @Test
    fun `test search by username`() {
        val user1 = User(
            login = "testuser1",
            id = 123,
            avatarUrl = "https://example.com/avatar1.jpg",
            name = "Test User 1",
            bio = "Test bio 1",
            followers = 100,
            following = 50,
            publicRepos = 10,
            createdAt = "2020-01-01T00:00:00Z"
        )
        
        val user2 = User(
            login = "testuser2",
            id = 456,
            avatarUrl = "https://example.com/avatar2.jpg",
            name = "Test User 2",
            bio = "Test bio 2",
            followers = 200,
            following = 100,
            publicRepos = 20,
            createdAt = "2021-01-01T00:00:00Z"
        )
        
        userCache.addUser(user1)
        userCache.addUser(user2)
        
        val results = userCache.searchByUsername("user1")
        assertEquals(1, results.size)
        assertEquals("testuser1", results[0].login)
    }
}