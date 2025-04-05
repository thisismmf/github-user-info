package com.github.userinfo.cache

import com.github.userinfo.api.model.User
import com.github.userinfo.api.model.Repository

class UserCache {
    private val userCache = mutableMapOf<String, User>()
    
    fun addUser(user: User) {
        userCache[user.login.lowercase()] = user
    }
    
    fun getUser(username: String): User? {
        return userCache[username.lowercase()]
    }
    
    fun hasUser(username: String): Boolean {
        return userCache.containsKey(username.lowercase())
    }
    
    fun getAllUsers(): List<User> {
        return userCache.values.toList()
    }
    
    fun searchByUsername(query: String): List<User> {
        return userCache.values.filter { 
            it.login.contains(query, ignoreCase = true) 
        }
    }
    
    fun searchByRepositoryName(query: String): Map<User, List<Repository>> {
        val results = mutableMapOf<User, List<Repository>>()
        
        for (user in userCache.values) {
            val matchingRepos = user.repositories.filter { 
                it.name.contains(query, ignoreCase = true) 
            }
            if (matchingRepos.isNotEmpty()) {
                results[user] = matchingRepos
            }
        }
        
        return results
    }
}