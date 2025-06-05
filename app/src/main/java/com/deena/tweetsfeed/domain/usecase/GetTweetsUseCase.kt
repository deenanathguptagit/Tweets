package com.deena.tweetsfeed.domain.usecase

import com.deena.tweetsfeed.data.model.TweetList
import com.deena.tweetsfeed.data.repository.TweetsRepository
import com.deena.tweetsfeed.utils.Resource
import javax.inject.Inject

class GetTweetsUseCase @Inject constructor(
    private val repository: TweetsRepository
) {
    suspend operator fun invoke(): Resource<TweetList> {
        return repository.getTweets()
    }
}
