package com.deena.tweetsfeed.domain.repository

import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.data.model.TweetList
import com.deena.tweetsfeed.utils.Resource

interface TweetsRepository {
    suspend fun getTweets(category: String): Resource<List<Tweet>>
    suspend fun getCategoryTweets(): Resource<List<String>>
}