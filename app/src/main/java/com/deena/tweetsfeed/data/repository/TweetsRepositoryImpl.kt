package com.deena.tweetsfeed.data.repository

import com.deena.tweetsfeed.data.model.TweetList
import com.deena.tweetsfeed.data.network.TweetsApiService
import com.deena.tweetsfeed.utils.Resource
import com.deena.tweetsfeed.utils.safeApiCallWithValidation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TweetsRepositoryImpl @Inject constructor(
    private val apiService: TweetsApiService
) : TweetsRepository {

    override suspend fun getTweets(): Resource<TweetList> {
        return safeApiCallWithValidation(
            apiCall = { apiService.getTweets() },
            validation = { tweetList -> tweetList.tweets.isNotEmpty() },
            emptyDataMessage = "No tweets available"
        )
    }
}
