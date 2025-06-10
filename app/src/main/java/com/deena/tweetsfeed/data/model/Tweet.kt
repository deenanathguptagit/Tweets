package com.deena.tweetsfeed.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Tweet(
    val category: String,
    val isMotivational: Boolean,
    val likes: Int,
    val retweets: Int,
    val subcategory: String,
    val text: String
)

@Serializable
data class TweetList(
    val tweets: List<Tweet>
)
