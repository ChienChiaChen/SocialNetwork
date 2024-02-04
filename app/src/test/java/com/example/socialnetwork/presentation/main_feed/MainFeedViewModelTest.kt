package com.example.socialnetwork.presentation.main_feed

import app.cash.turbine.test
import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.exception.getStringResId
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.usecase.post.FetchAllPostUseCase
import com.example.socialnetwork.presentation.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainFeedViewModelTest {

    private lateinit var viewModel: MainFeedViewModel

    @MockK
    private lateinit var fetchAllPostUseCase: FetchAllPostUseCase

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)


    @Before
    fun setup() {
        // Turn relaxUnitFun on for all mocks
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = MainFeedViewModel(fetchAllPostUseCase)
    }

    @Test
    fun fetchPostSuccessfully() {
        testCoroutineRule.runBlockingTest {
            val fakePost = listOf(Post(id = "1"), Post(id = "2"))
            val fakeResult = DataResult.Success(fakePost)
            coEvery {
                fetchAllPostUseCase.invoke()
            } returns fakeResult

            // when
            viewModel.onEvent(MainFeedContract.MainFeedEvent.RefreshPost)

            viewModel.mainFeedDataState.test {
                val actual1 = awaitItem()
                Assert.assertEquals(fakePost, actual1.post)
                Assert.assertEquals(false, actual1.isLoading)
                expectNoEvents()
            }
        }
    }

    @Test
    fun fetchPostFailed() {
        testCoroutineRule.runBlockingTest {
            val fakeErrorMsg = NetworkException.NetworkUnavailable.getStringResId()
            val fakeResult =
                DataResult.Error<List<Post>>(exception = NetworkException.NetworkUnavailable)
            coEvery {
                fetchAllPostUseCase.invoke()
            } returns fakeResult

            // when
            viewModel.onEvent(MainFeedContract.MainFeedEvent.RefreshPost)

            viewModel.mainFeedDataState.test {
                val actual = awaitItem().errorMsg
                Assert.assertEquals(fakeErrorMsg, actual)
                expectNoEvents()
            }
        }
    }



}