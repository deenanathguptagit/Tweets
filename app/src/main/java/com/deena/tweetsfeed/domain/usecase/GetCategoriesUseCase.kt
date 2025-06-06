package com.deena.tweetsfeed.domain.usecase

import com.deena.tweetsfeed.domain.repository.TweetsRepository
import com.deena.tweetsfeed.utils.Resource
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: TweetsRepository
) {
    suspend operator fun invoke(): Resource<List<String>> {
        return repository.getCategoryTweets()
    }
}
