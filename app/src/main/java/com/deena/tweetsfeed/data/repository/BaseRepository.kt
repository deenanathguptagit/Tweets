package com.deena.tweetsfeed.data.repository

import com.deena.tweetsfeed.utils.Resource
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Resource<T> {
        return safeApiCallWithValidation(apiCall)
    }

    protected suspend fun <T> safeApiCallWithValidation(
        apiCall: suspend () -> Response<T>,
        validation: (T) -> Boolean = { true },
        emptyDataMessage: String = "No data available"
    ): Resource<T> {
        return try {
            val response = apiCall()
            response.toResource(validation, emptyDataMessage)
        } catch (e: HttpException) {
            Resource.Error("Network error: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Connection error. Please check your internet connection")
        } catch (e: Exception) {
            Resource.Error("An unexpected error occurred: ${e.localizedMessage}")
        }
    }
}

private fun <T> Response<T>.toResource(
    validation: (T) -> Boolean = { true },
    emptyDataMessage: String = "No data available"
): Resource<T> {
    return when {
        isSuccessful -> {
            body()?.let { data ->
                if (validation(data)) {
                    Resource.Success(data)
                } else {
                    Resource.Error(emptyDataMessage)
                }
            } ?: Resource.Error("Response body is null")
        }
        else -> handleErrorResponse()
    }
}

private fun <T> Response<T>.handleErrorResponse(): Resource<T> {
    return when (code()) {
        401 -> Resource.Error("Unauthorized access")
        403 -> Resource.Error("Access forbidden")
        404 -> Resource.Error("Resource not found")
        in 500..599 -> Resource.Error("Server error. Please try again later")
        else -> Resource.Error("Failed to fetch data: ${message()}")
    }
}
