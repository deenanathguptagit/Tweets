package com.deena.tweetsfeed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.domain.usecase.GetTweetsUseCase
import com.deena.tweetsfeed.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TweetsUiState(
    val isLoading: Boolean = false,
    val tweets: List<Tweet> = emptyList(),
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)

sealed class TweetsEvent {
    data class RetryLoadTweets(val category: String) : TweetsEvent()
    object RefreshTweets : TweetsEvent()
    data class LikeTweet(val tweetIndex: Int) : TweetsEvent()
    data class RetweetTweet(val tweetIndex: Int) : TweetsEvent()
    data class LoadTweetsByCategory(val category: String) : TweetsEvent()
}

@HiltViewModel
class TweetsViewModel @Inject constructor(
    private val getTweetsUseCase: GetTweetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TweetsUiState(isLoading = true))
    val uiState: StateFlow<TweetsUiState> = _uiState.asStateFlow()

    fun onEvent(event: TweetsEvent) {
        when (event) {
            is TweetsEvent.RetryLoadTweets -> {
                retryGetTweets(event.category)
            }

            is TweetsEvent.RefreshTweets -> {
                refreshTweets()
            }

            is TweetsEvent.LikeTweet -> {
                likeTweet(event.tweetIndex)
            }

            is TweetsEvent.RetweetTweet -> {
                retweetTweet(event.tweetIndex)
            }

            is TweetsEvent.LoadTweetsByCategory -> {
                getTweetsByCategory(event.category)
            }
        }
    }

    private fun getTweets(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = getTweetsUseCase(category)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tweets = result.data ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun getTweetsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = getTweetsUseCase(category)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tweets = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun retryGetTweets(category: String) {
        getTweets(category)
    }

    private fun refreshTweets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)

            when (val result = getTweetsUseCase("")) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        tweets = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isRefreshing = true)
                }
            }
        }
    }

    private fun likeTweet(tweetIndex: Int) {
        val currentTweets = _uiState.value.tweets.toMutableList()
        if (tweetIndex in currentTweets.indices) {
            val tweet = currentTweets[tweetIndex]
            currentTweets[tweetIndex] = tweet.copy(likes = tweet.likes + 1)
            _uiState.value = _uiState.value.copy(tweets = currentTweets)
        }
    }

    private fun retweetTweet(tweetIndex: Int) {
        val currentTweets = _uiState.value.tweets.toMutableList()
        if (tweetIndex in currentTweets.indices) {
            val tweet = currentTweets[tweetIndex]
            currentTweets[tweetIndex] = tweet.copy(retweets = tweet.retweets + 1)
            _uiState.value = _uiState.value.copy(tweets = currentTweets)
        }
    }
}
