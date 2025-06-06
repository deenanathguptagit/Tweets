package com.deena.tweetsfeed.domain.usecase

import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.data.model.TweetList
import com.deena.tweetsfeed.domain.repository.TweetsRepository
import com.deena.tweetsfeed.utils.Resource
import javax.inject.Inject

class GetTweetsUseCase @Inject constructor(
    private val repository: TweetsRepository
) {
    suspend operator fun invoke(category: String): Resource<List<Tweet>> {
        return repository.getTweets("\$..tweets[?(@.category==\"$category\")]")
    }
}
