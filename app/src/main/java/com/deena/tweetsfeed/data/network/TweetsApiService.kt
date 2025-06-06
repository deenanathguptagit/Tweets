package com.deena.tweetsfeed.data.network

import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.data.model.TweetList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TweetsApiService {
    @GET("v3/b/670193a8ad19ca34f8b35791?meta=false")
    suspend fun getTweets(@Header("X-JSON-Path") category: String): Response<List<Tweet>>

    @GET("v3/b/670193a8ad19ca34f8b35791?meta=false")
    @Headers("X-JSON-Path:tweets..category")
    suspend fun getCategoryTweets(): Response<List<String>>
}
