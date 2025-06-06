package com.deena.tweetsfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deena.tweetsfeed.presentation.screen.CategoriesScreen
import com.deena.tweetsfeed.presentation.screen.TweetsScreen
import com.deena.tweetsfeed.ui.theme.TweetsFeedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TweetsFeedTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TweetsFeedNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TweetsFeedNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "categories",
        modifier = modifier
    ) {
        composable("categories") {
            CategoriesScreen(
                onCategoryClick = { category ->
                    navController.navigate("tweets/$category")
                }
            )
        }

        composable("tweets/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            TweetsScreen(
                category = category,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
