package com.deena.tweetsfeed.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.presentation.viewmodel.TweetsEvent
import com.deena.tweetsfeed.presentation.viewmodel.TweetsViewModel

@Composable
fun TweetsScreen(
    modifier: Modifier = Modifier,
    viewModel: TweetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tweets Feed",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${uiState.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = { onEvent(TweetsEvent.RetryLoadTweets) }
                    ) {
                        Text("Retry")
                    }
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(uiState.tweets) { index, tweet ->
                        TweetItem(
                            tweet = tweet,
                            onLikeClick = { onEvent(TweetsEvent.LikeTweet(index)) },
                            onRetweetClick = { onEvent(TweetsEvent.RetweetTweet(index)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TweetItem(
    tweet: Tweet,
    onLikeClick: () -> Unit,
    onRetweetClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = tweet.text,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Category: ${tweet.category}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                if (tweet.isMotivational) {
                    Text(
                        text = "Motivational",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onLikeClick
                ) {
                    Text(
                        text = "❤️ ${tweet.likes}",
                        fontSize = 12.sp
                    )
                }
                TextButton(
                    onClick = onRetweetClick
                ) {
                    Text(
                        text = "🔄 ${tweet.retweets}",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
