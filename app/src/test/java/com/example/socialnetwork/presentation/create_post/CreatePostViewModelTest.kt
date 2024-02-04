package com.example.socialnetwork.presentation.create_post

import app.cash.turbine.test
import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.exception.getStringResId
import com.example.socialnetwork.common.result.CreatePostResult
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.usecase.post.CreatePostUseCase
import com.example.socialnetwork.presentation.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime


@ExperimentalTime
class CreatePostViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: CreatePostViewModel

    @MockK
    private lateinit var createPost: CreatePostUseCase

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    @Before
    fun setup() {
        // Turn relaxUnitFun on for all mocks
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = CreatePostViewModel(createPost)
    }

    //region create post
    @Test
    fun createPostSuccessfully() {
        runTest {
            testScope.launch {
                val fakeResult = CreatePostResult(result = DataResult.Success(Unit))
                coEvery {
                    createPost.invoke(any(), any())
                } returns fakeResult
                viewModel.onEvent(CreatePostContract.CreatePostEvent.Post)

                viewModel.effect.test {
                    val actual = awaitItem()
                    Assert.assertEquals(CreatePostContract.CreatePostEffect.NavigateTo, actual)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun createPostFailedWithBadRequest() {
        runTest {
            testScope.launch {
                val fakeErrorMsg = NetworkException.BadRequest.getStringResId()
                val fakeResult = CreatePostResult(result= DataResult.Error(NetworkException.BadRequest))
                coEvery {
                    createPost.invoke(any(), any())
                } returns fakeResult

                viewModel.onEvent(CreatePostContract.CreatePostEvent.Post)

                viewModel.descriptionState.test {
                    val actual = awaitItem()
                    Assert.assertEquals(fakeErrorMsg, actual.error)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun createPostFailedWithNotAuthorized() {
        runTest {
            testScope.launch {
                val fakeErrorMsg = NetworkException.NotAuthorized.getStringResId()
                val fakeResult = CreatePostResult(result= DataResult.Error(NetworkException.NotAuthorized))
                coEvery {
                    createPost.invoke(any(), any())
                } returns fakeResult

                viewModel.onEvent(CreatePostContract.CreatePostEvent.Post)

                viewModel.descriptionState.test {
                    val actual = awaitItem()
                    Assert.assertEquals(fakeErrorMsg, actual.error)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }


    @Test
    fun createPostFailedWithNetworkIssue() {
        runTest {
            testScope.launch {
                val fakeErrorMsg = NetworkException.NetworkUnavailable.getStringResId()
                val fakeResult = CreatePostResult(result= DataResult.Error(NetworkException.NetworkUnavailable))
                coEvery {
                    createPost.invoke(any(), any())
                } returns fakeResult

                viewModel.onEvent(CreatePostContract.CreatePostEvent.Post)

                viewModel.descriptionState.test {
                    val actual = awaitItem()
                    Assert.assertEquals(fakeErrorMsg, actual.error)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun createPostFailedWithNotFound() {
        runTest {
            testScope.launch {
                val fakeErrorMsg = NetworkException.NotFound.getStringResId()
                val fakeResult = CreatePostResult(result= DataResult.Error(NetworkException.NotFound))
                coEvery {
                    createPost.invoke(any(), any())
                } returns fakeResult

                viewModel.onEvent(CreatePostContract.CreatePostEvent.Post)

                viewModel.descriptionState.test {
                    val actual = awaitItem()
                    Assert.assertEquals(fakeErrorMsg, actual.error)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }
    //endregion


}