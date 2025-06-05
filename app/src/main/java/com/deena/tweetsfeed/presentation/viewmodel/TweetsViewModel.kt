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

@HiltViewModel
class TweetsViewModel @Inject constructor(
    private val getTweetsUseCase: GetTweetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TweetsUiState(isLoading = true))
    val uiState: StateFlow<TweetsUiState> = _uiState.asStateFlow()

    init {
        getTweets()
    }

    fun getTweets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = getTweetsUseCase()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tweets = result.data?.tweets ?: emptyList()
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

    fun retryGetTweets() {
        getTweets()
    }
}
