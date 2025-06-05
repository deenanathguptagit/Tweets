package com.deena.tweetsfeed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deena.tweetsfeed.data.model.TweetList
import com.deena.tweetsfeed.domain.usecase.GetTweetsUseCase
import com.deena.tweetsfeed.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweetsViewModel @Inject constructor(
    private val getTweetsUseCase: GetTweetsUseCase
) : ViewModel() {

    private val _tweetsState = MutableStateFlow<Resource<TweetList>>(Resource.Loading())
    val tweetsState: StateFlow<Resource<TweetList>> = _tweetsState.asStateFlow()

    init {
        getTweets()
    }

    fun getTweets() {
        viewModelScope.launch {
            _tweetsState.value = Resource.Loading()
            _tweetsState.value = getTweetsUseCase()
        }
    }

    fun retryGetTweets() {
        getTweets()
    }
}
