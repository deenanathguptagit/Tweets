package com.deena.tweetsfeed.di

import com.deena.tweetsfeed.data.repository.TweetsRepository
import com.deena.tweetsfeed.data.repository.TweetsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTweetsRepository(
        tweetsRepositoryImpl: TweetsRepositoryImpl
    ): TweetsRepository
}
