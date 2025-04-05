package com.github.userinfo.ui

import com.github.userinfo.api.GitHubApiService
import com.github.userinfo.api.model.User
import com.github.userinfo.api.model.Repository
import com.github.userinfo.cache.UserCache
import com.github.userinfo.util.ErrorHandler
import kotlinx.coroutines.runBlocking
import java.util.Scanner

class Menu(
    private val apiService: GitHubApiService,
    private val userCache: UserCache,
    private val errorHandler: ErrorHandler
) {
    private val scanner = Scanner(System.`in`)

    fun start() {
        println("GitHub User Information Application")
        var running = true
        
        while (running) {
            displayMainMenu()
            when (readOption()) {
                1 -> getUserInformation()
                2 -> displayCachedUsers()
                3 -> searchByUsername()
                4 -> searchByRepositoryName()
                5 -> {
                    println("Exiting application. Goodbye!")
                    running = false
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    private fun displayMainMenu() {
        println("\n=== Main Menu ===")
        println("1️⃣ Get user information based on username")
        println("2️⃣ Display list of users in memory")
        println("3️⃣ Search by username among users in memory")
        println("4️⃣ Search by repository name among data in memory")
        println("5️⃣ Exit the program")
        print("Enter your choice: ")
    }

    private fun readOption(): Int {
        return try {
            scanner.nextLine().toInt()
        } catch (e: NumberFormatException) {
            -1
        }
    }

    private fun getUserInformation() {
        println("\nEnter GitHub username: ")
        val username = scanner.nextLine().trim()
        
        if (username.isEmpty()) {
            println("Username cannot be empty.")
            return
        }

        if (userCache.hasUser(username)) {
            println("User found in cache:")
            displayUser(userCache.getUser(username)!!)
            return
        }

        println("Fetching user data...")
        try {
            runBlocking {
                val userResult = errorHandler.handleApiResponse(apiService.getUser(username))
                
                if (userResult.isSuccess) {
                    val user = userResult.getOrNull()!!
                    val reposResult = errorHandler.handleApiResponse(apiService.getUserRepositories(username))
                    
                    if (reposResult.isSuccess) {
                        user.repositories = reposResult.getOrNull()!!
                        userCache.addUser(user)
                        displayUser(user)
                    } else {
                        println("Error fetching repositories: ${reposResult.exceptionOrNull()?.message}")
                        // Still cache the user even without repositories
                        user.repositories = emptyList()
                        userCache.addUser(user)
                        displayUser(user)
                    }
                } else {
                    println("Error: ${userResult.exceptionOrNull()?.message}")
                }
            }
        } catch (e: Exception) {
            println(errorHandler.handleException(e))
        }
    }

    private fun displayCachedUsers() {
        val users = userCache.getAllUsers()
        if (users.isEmpty()) {
            println("\nNo users in memory. Fetch some users first.")
            return
        }

        println("\n=== Cached Users ===")
        users.forEachIndexed { index, user ->
            println("${index + 1}. ${user.login} (${user.name ?: "N/A"})")
        }
        
        println("\nEnter user number for details or 0 to return: ")
        val choice = readOption()
        
        if (choice in 1..users.size) {
            displayUser(users[choice - 1])
        } else if (choice != 0) {
            println("Invalid selection.")
        }
    }

    private fun searchByUsername() {
        if (userCache.getAllUsers().isEmpty()) {
            println("\nNo users in memory. Fetch some users first.")
            return
        }
        
        println("\nEnter search query: ")
        val query = scanner.nextLine().trim()
        
        if (query.isEmpty()) {
            println("Search query cannot be empty.")
            return
        }
        
        val results = userCache.searchByUsername(query)
        if (results.isEmpty()) {
            println("No users found matching '$query'.")
            return
        }
        
        println("\n=== Search Results ===")
        results.forEachIndexed { index, user ->
            println("${index + 1}. ${user.login} (${user.name ?: "N/A"})")
        }
        
        println("\nEnter user number for details or 0 to return: ")
        val choice = readOption()
        
        if (choice in 1..results.size) {
            displayUser(results[choice - 1])
        } else if (choice != 0) {
            println("Invalid selection.")
        }
    }

    private fun searchByRepositoryName() {
        if (userCache.getAllUsers().isEmpty()) {
            println("\nNo users in memory. Fetch some users first.")
            return
        }
        
        println("\nEnter repository name to search: ")
        val query = scanner.nextLine().trim()
        
        if (query.isEmpty()) {
            println("Search query cannot be empty.")
            return
        }
        
        val results = userCache.searchByRepositoryName(query)
        if (results.isEmpty()) {
            println("No repositories found matching '$query'.")
            return
        }
        
        println("\n=== Repository Search Results ===")
        val userList = results.keys.toList()
        
        userList.forEachIndexed { userIndex, user ->
            println("${userIndex + 1}. ${user.login} (${user.name ?: "N/A"})")
            results[user]?.forEachIndexed { repoIndex, repo ->
                println("   ${userIndex + 1}.${repoIndex + 1}. ${repo.name}")
            }
        }
        
        println("\nEnter user number to view details or 0 to return: ")
        val userChoice = readOption()
        
        if (userChoice in 1..userList.size) {
            val selectedUser = userList[userChoice - 1]
            val repos = results[selectedUser]!!
            
            println("\nRepositories for user ${selectedUser.login}:")
            repos.forEachIndexed { index, repo ->
                println("${index + 1}. ${repo.name}")
            }
            
            println("\nEnter repository number for details or 0 to return: ")
            val repoChoice = readOption()
            
            if (repoChoice in 1..repos.size) {
                displayRepository(repos[repoChoice - 1])
            } else if (repoChoice != 0) {
                println("Invalid selection.")
            }
        } else if (userChoice != 0) {
            println("Invalid selection.")
        }
    }

    private fun displayUser(user: User) {
        println("\n=== User Details ===")
        println(user.toString())
        
        if (user.repositories.isNotEmpty()) {
            println("\nRepositories (${user.repositories.size}):")
            user.repositories.take(5).forEachIndexed { index, repo ->
                println("${index + 1}. ${repo.name}")
            }
            
            if (user.repositories.size > 5) {
                println("...and ${user.repositories.size - 5} more.")
            }
            
            println("\nEnter repository number to view details or 0 to return: ")
            val choice = readOption()
            
            if (choice in 1..minOf(5, user.repositories.size)) {
                displayRepository(user.repositories[choice - 1])
            } else if (choice != 0) {
                println("Invalid selection.")
            }
        }
    }

    private fun displayRepository(repo: Repository) {
        println("\n=== Repository Details ===")
        println(repo.toString())
        println("\nPress Enter to continue...")
        scanner.nextLine()
    }
}