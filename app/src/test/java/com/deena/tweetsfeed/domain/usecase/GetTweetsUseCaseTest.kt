package com.deena.tweetsfeed.domain.usecase

import com.deena.tweetsfeed.data.model.Tweet
import com.deena.tweetsfeed.data.network.TweetsApiService
import com.deena.tweetsfeed.data.repository.TweetsRepositoryImpl
import com.deena.tweetsfeed.domain.repository.TweetsRepository
import com.deena.tweetsfeed.utils.Resource
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class GetTweetsUseCaseTest {
    private lateinit var repository: TweetsRepository

    @Mock
    private lateinit var apiService: TweetsApiService

    private lateinit var getTweetsUseCase: GetTweetsUseCase

    private lateinit var fakeTweet: Tweet

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fakeTweet = Tweet(
            category = "Inspiration",
            isMotivational = true,
            likes = 120,
            retweets = 45,
            subcategory = "Morning Motivation",
            text = "Start your day with purpose and determination. The world is yours to conquer!"
        )
        repository = TweetsRepositoryImpl(apiService)
        getTweetsUseCase = GetTweetsUseCase(repository)
    }

    @After
    fun tearDown() {
        fakeTweet = Tweet(
            category = "",
            isMotivational = false,
            likes = 0,
            retweets = 0,
            subcategory = "",
            text = ""
        )
    }

    @Test
    fun getAllTweets_Success() = runTest {
        whenever(repository.getTweets("$..tweets[?(@.category==\"Inspiration\")]"))
            .thenReturn(Resource.Success(listOf(fakeTweet)))

        val result = getTweetsUseCase.invoke("Inspiration")

        assertTrue(result.data?.isNotEmpty() == true)
        assertEquals("Inspiration", result.data?.first()?.category)
    }

    @Test
    fun `getTweets returns success when API returns data`() = runTest {
        val category = "Inspiration"
        val expectedHeaderValue = "$..tweets[?(@.category==\"$category\")]"
        val dummyTweet = Tweet("Inspiration", true, 100, 50, "Morning", "Wake up and crush it!")

        whenever(apiService.getTweets(expectedHeaderValue))
            .thenReturn(Response.success(listOf(dummyTweet)))

        val result = getTweetsUseCase.invoke(category)

        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size ?: 0)
    }


    @Test
    fun getAllTweets_Error() = runTest {
        `when`(repository.getTweets("$..tweets[?(@.category==Inspiration)]"))
            .thenReturn(Resource.Error("Unknown error"))

        val result = getTweetsUseCase.invoke("Inspiration")

        assertTrue(result is Resource.Error)
    }
}