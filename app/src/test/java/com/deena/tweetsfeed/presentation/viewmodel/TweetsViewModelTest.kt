package com.deena.tweetsfeed.presentation.viewmodel

import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.domain.usecase.GetCategoriesUseCase
import com.deena.tweetsfeed.domain.usecase.GetTweetsUseCase
import com.deena.tweetsfeed.utils.Resource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class TweetsViewModelTest {

    @Mock
    private lateinit var getTweetsUseCase: GetTweetsUseCase

    @Mock
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    private lateinit var viewModel: TweetsViewModel

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var closeable: AutoCloseable

    @Before
    fun setup() {
        // ✅ Don't wrap this in runTest
        Dispatchers.setMain(testDispatcher)

        // ✅ Initialize mocks before using them
        closeable = MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        closeable.close()
    }

    @Test
    fun `init loads categories into uiState`() = runTest {
        // ✅ Set up suspend mock in coroutine scope
        whenever(getCategoriesUseCase()).thenReturn(Resource.Success(listOf("Tech", "Sports")))

        // ✅ Initialize ViewModel AFTER mocks are ready
        viewModel = TweetsViewModel(getTweetsUseCase, getCategoriesUseCase)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.categoriesLoading)
        assertEquals(listOf("Tech", "Sports"), state.categories)
    }

    @Test
    fun `load tweets by category updates state with tweets`() = runTest {
        val tweets = listOf(Tweet(category = "Tech", text = "Hello", likes = 10, retweets = 10, subcategory = "Tech", isMotivational = true))
        `when`(getTweetsUseCase("Tech")).thenReturn(Resource.Success(tweets))

        viewModel.onEvent(TweetsEvent.LoadTweetsByCategory("Tech"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Tech", state.selectedCategory)
        assertEquals(tweets, state.tweets)
    }
}
