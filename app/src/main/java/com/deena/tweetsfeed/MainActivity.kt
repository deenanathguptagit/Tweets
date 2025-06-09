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
import androidx.navigation.toRoute
import com.deena.tweetsfeed.presentation.screen.CategoriesScreen
import com.deena.tweetsfeed.presentation.screen.TweetsScreen
import com.deena.tweetsfeed.ui.theme.TweetsFeedTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

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

@Serializable
object CategoriesRoute

@Serializable
data class TweetsRoute(val category: String)

@Composable
fun TweetsFeedNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = CategoriesRoute,
        modifier = modifier
    ) {
        composable<CategoriesRoute> {
            CategoriesScreen(
                onCategoryClick = { category ->
                    navController.navigate(TweetsRoute(category))
                }
            )
        }

        composable<TweetsRoute> { backStackEntry ->
            val tweetsRoute = backStackEntry.toRoute<TweetsRoute>()
            TweetsScreen(
                category = tweetsRoute.category,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
