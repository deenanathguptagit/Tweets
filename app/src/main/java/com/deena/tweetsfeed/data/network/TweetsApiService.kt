package com.deena.tweetsfeed.data.network

import com.deena.tweetsfeed.data.model.TweetList
import retrofit2.Response
import retrofit2.http.GET

interface TweetsApiService {
    @GET("v3/b/670193a8ad19ca34f8b35791?meta=false")
    suspend fun getTweets(): Response<TweetList>
}
