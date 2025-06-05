package com.deena.tweetsfeed.data.repository

import com.deena.tweetsfeed.data.model.TweetList
import com.deena.tweetsfeed.utils.Resource

interface TweetsRepository {
    suspend fun getTweets(): Resource<TweetList>
    suspend fun getCategoryTweets(): Resource<List<String>>
}