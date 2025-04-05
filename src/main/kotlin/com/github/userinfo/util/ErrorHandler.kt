package com.github.userinfo.util

import retrofit2.Response
import java.io.IOException

class ErrorHandler {
    fun <T> handleApiResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(IOException("Response body is null"))
            }
        } else {
            when (response.code()) {
                404 -> Result.failure(IOException("Resource not found"))
                403 -> Result.failure(IOException("API rate limit exceeded"))
                401 -> Result.failure(IOException("Unauthorized access"))
                else -> Result.failure(IOException("API error: ${response.code()} - ${response.message()}"))
            }
        }
    }

    fun handleException(e: Exception): String {
        return when (e) {
            is IOException -> "Network error: ${e.message}"
            else -> "Unexpected error: ${e.message}"
        }
    }
}