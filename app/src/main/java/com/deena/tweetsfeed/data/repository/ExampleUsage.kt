package com.deena.tweetsfeed.data.repository

import com.deena.tweetsfeed.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

// Example of how to use the generic BaseRepository for other API calls

/*
// Example User data model
@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Example API service
interface UserApiService {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<User>
    
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}

// Example repository implementation
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService
) : BaseRepository(), UserRepository {

    // Simple API call without validation
    override suspend fun getUser(userId: Int): Resource<User> {
        return safeApiCall { apiService.getUser(userId) }
    }

    // API call with custom validation
    override suspend fun getUsers(): Resource<List<User>> {
        return safeApiCallWithValidation(
            apiCall = { apiService.getUsers() },
            validation = { users -> users.isNotEmpty() },
            emptyDataMessage = "No users found"
        )
    }

    // API call with custom validation for active users only
    override suspend fun getActiveUsers(): Resource<List<User>> {
        return safeApiCallWithValidation(
            apiCall = { apiService.getUsers() },
            validation = { users -> 
                users.isNotEmpty() && users.any { it.email.isNotEmpty() }
            },
            emptyDataMessage = "No active users available"
        )
    }
}
*/