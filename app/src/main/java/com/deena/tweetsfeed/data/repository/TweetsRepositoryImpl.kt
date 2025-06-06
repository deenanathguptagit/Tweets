package com.deena.tweetsfeed.data.repository

import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.data.model.TweetList
import com.deena.tweetsfeed.data.network.TweetsApiService
import com.deena.tweetsfeed.domain.repository.TweetsRepository
import com.deena.tweetsfeed.utils.Resource
import com.deena.tweetsfeed.utils.safeApiCallWithValidation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TweetsRepositoryImpl @Inject constructor(
    private val apiService: TweetsApiService
) : TweetsRepository {

    override suspend fun getTweets(category: String): Resource<List<Tweet>> {
        return when (val result = safeApiCallWithValidation(
            apiCall = { apiService.getTweets(category) },
            validation = { tweetList -> tweetList.isNotEmpty() },
            emptyDataMessage = "No tweets available"
        )) {
            is Resource.Success -> Resource.Success(result.data ?: emptyList())
            is Resource.Error -> Resource.Error(result.message ?: "Unknown error")
            is Resource.Loading -> Resource.Loading()
        }
    }

    override suspend fun getCategoryTweets(): Resource<List<String>> {
        return safeApiCallWithValidation(
            apiCall = { apiService.getCategoryTweets() },
            validation = { tweetCategory -> tweetCategory.isNotEmpty() },
            emptyDataMessage = "No tweets categories available"
        )
    }
}
