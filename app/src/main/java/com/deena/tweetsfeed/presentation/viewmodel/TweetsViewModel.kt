package com.deena.tweetsfeed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.domain.usecase.GetCategoriesUseCase
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
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val categoriesLoading: Boolean = false,
    val categoriesError: String? = null
)

sealed class TweetsEvent {
    object LoadCategories : TweetsEvent()
    object RetryLoadCategories : TweetsEvent()
    data class LoadTweetsByCategory(val category: String) : TweetsEvent()
    data class RetryLoadTweets(val category: String) : TweetsEvent()
    object RefreshTweets : TweetsEvent()
    data class LikeTweet(val tweetIndex: Int) : TweetsEvent()
    data class RetweetTweet(val tweetIndex: Int) : TweetsEvent()
}

@HiltViewModel
class TweetsViewModel @Inject constructor(
    private val getTweetsUseCase: GetTweetsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TweetsUiState(categoriesLoading = true))
    val uiState: StateFlow<TweetsUiState> = _uiState.asStateFlow()

    init {
        onEvent(TweetsEvent.LoadCategories)
    }

    fun onEvent(event: TweetsEvent) {
        when (event) {
            is TweetsEvent.LoadCategories -> {
                getCategories()
            }

            is TweetsEvent.RetryLoadCategories -> {
                getCategories()
            }

            is TweetsEvent.LoadTweetsByCategory -> {
                getTweetsByCategory(event.category)
            }

            is TweetsEvent.RetryLoadTweets -> {
                retryGetTweets()
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
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(categoriesLoading = true, categoriesError = null)

            when (val result = getCategoriesUseCase()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        categoriesLoading = false,
                        categories = result.data ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        categoriesLoading = false,
                        categoriesError = result.message
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(categoriesLoading = true)
                }
            }
        }
    }

    private fun getTweetsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                selectedCategory = category,
                tweets = emptyList()
            )

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

    private fun retryGetTweets() {
        _uiState.value.selectedCategory?.let { category ->
            getTweetsByCategory(category)
        }
    }

    private fun refreshTweets() {
        viewModelScope.launch {
            _uiState.value.selectedCategory?.let { category ->
                _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)

                when (val result = getTweetsUseCase(category)) {
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
