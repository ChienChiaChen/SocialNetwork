package com.example.socialnetwork.presentation.profile

import app.cash.turbine.test
import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.exception.getStringResId
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.User
import com.example.socialnetwork.domain.usecase.auth.user.FetchCurrentUserCase
import com.example.socialnetwork.domain.usecase.auth.user.UpdateCurrentUserCase
import com.example.socialnetwork.domain.usecase.post.FetchCurrentUserPostUseCase
import com.example.socialnetwork.presentation.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel

    @MockK
    private lateinit var fetchCurrentUserPostUseCase: FetchCurrentUserPostUseCase

    @MockK
    private lateinit var fetchCurrentUserCase: FetchCurrentUserCase

    @MockK
    private lateinit var updateCurrentUserCase: UpdateCurrentUserCase

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        // Turn relaxUnitFun on for all mocks
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = ProfileViewModel(
            fetchCurrentUserPostUseCase,
            fetchCurrentUserCase,
            updateCurrentUserCase
        )
    }

    //region update image
    @Test
    fun updateImageUriSuccessfully() =
        testCoroutineRule.runBlockingTest {
            val fakeUser = User("jason")
            val fakeResult = DataResult.Success(fakeUser)
            coEvery {
                updateCurrentUserCase.invoke(any())
            } returns fakeResult

            // when
            viewModel.onEvent(ProfileContract.ProfileEvent.EnteredImageUri(""))

            viewModel.profileState.test {
                val actual1 = awaitItem()
                Assert.assertEquals(fakeUser.username, actual1.user.username)
                Assert.assertEquals(false, actual1.isLoading)
                expectNoEvents()
            }
        }


    @Test
    fun updateImageWithNetworkIssue() =
        testCoroutineRule.runBlockingTest {
            val fakeErrorMsg = NetworkException.NetworkUnavailable.getStringResId()
            val fakeResult =
                DataResult.Error<User>(exception = NetworkException.NetworkUnavailable)
            coEvery {
                updateCurrentUserCase.invoke(any())
            } returns fakeResult

            // when
            viewModel.onEvent(ProfileContract.ProfileEvent.EnteredImageUri(""))

            viewModel.profileState.test {
                val actual = awaitItem().errorMsg
                Assert.assertEquals(fakeErrorMsg, actual)
                expectNoEvents()
            }
        }
    //endregion


    //region fetch post
    @Test
    fun fetchAllPostSuccessfully() =
        testCoroutineRule.runBlockingTest {
            val fakePost = listOf(Post(id = "1"), Post(id = "2"))
            val fakeResult = DataResult.Success(fakePost)
            coEvery {
                fetchCurrentUserPostUseCase.invoke()
            } returns fakeResult

            // when
            viewModel.onEvent(ProfileContract.ProfileEvent.RefreshPost)

            viewModel.profileState.test {
                val actual1 = awaitItem()
                Assert.assertEquals(fakePost, actual1.post)
                Assert.assertEquals(false, actual1.isLoading)
                expectNoEvents()
            }
        }


    @Test
    fun fetchPostFailedWithNetworkIssue() =
        testCoroutineRule.runBlockingTest {
            val fakeErrorMsg = NetworkException.NetworkUnavailable.getStringResId()
            val fakeResult =
                DataResult.Error<List<Post>>(exception = NetworkException.NetworkUnavailable)
            coEvery {
                fetchCurrentUserPostUseCase.invoke()
            } returns fakeResult

            // when
            viewModel.onEvent(ProfileContract.ProfileEvent.RefreshPost)

            viewModel.profileState.test {
                val actual = awaitItem().errorMsg
                Assert.assertEquals(fakeErrorMsg, actual)
                expectNoEvents()
            }
        }


    @Test
    fun fetchPostFailedWithNotAuthorized() =
        testCoroutineRule.runBlockingTest {
            val fakeErrorMsg = NetworkException.NotAuthorized.getStringResId()
            val fakeResult =
                DataResult.Error<List<Post>>(exception = NetworkException.NotAuthorized)
            coEvery {
                fetchCurrentUserPostUseCase.invoke()
            } returns fakeResult

            // when
            viewModel.onEvent(ProfileContract.ProfileEvent.RefreshPost)

            viewModel.profileState.test {
                val actual = awaitItem().errorMsg
                Assert.assertEquals(fakeErrorMsg, actual)
                expectNoEvents()
            }
        }

    //endregion


    //region fetch current user
    @Test
    fun fetchCurrentUserCaseSuccessfully() = testCoroutineRule.runBlockingTest {
        val fakeUser = User("jason")
        val fakeResult = DataResult.Success(fakeUser)
        coEvery {
            fetchCurrentUserCase.invoke()
        } returns fakeResult

        // when
        viewModel.onEvent(ProfileContract.ProfileEvent.RefreshUserInfo)

        viewModel.profileState.test {
            val actual = awaitItem().user
            Assert.assertEquals(actual, fakeUser)
            expectNoEvents()
        }
    }

    @Test
    fun fetchUserFailedWithNetworkIssue() =
        testCoroutineRule.runBlockingTest {
            val fakeErrorMsg = NetworkException.NotAuthorized.getStringResId()
            val fakeResult =
                DataResult.Error<User>(exception = NetworkException.NotAuthorized)
            coEvery {
                fetchCurrentUserCase.invoke()
            } returns fakeResult

            // when
            viewModel.onEvent(ProfileContract.ProfileEvent.RefreshUserInfo)

            viewModel.profileState.test {
                val actual = awaitItem().errorMsg
                Assert.assertEquals(fakeErrorMsg, actual)
                expectNoEvents()
            }
        }


    @Test
    fun fetchUserFailedWithNotAuthorized() =
        testCoroutineRule.runBlockingTest {
            val fakeErrorMsg = NetworkException.NetworkUnavailable.getStringResId()
            val fakeResult =
                DataResult.Error<User>(exception = NetworkException.NetworkUnavailable)
            coEvery {
                fetchCurrentUserCase.invoke()
            } returns fakeResult

            // when
            viewModel.onEvent(ProfileContract.ProfileEvent.RefreshUserInfo)

            viewModel.profileState.test {
                val actual = awaitItem().errorMsg
                Assert.assertEquals(fakeErrorMsg, actual)
                expectNoEvents()
            }
        }
    //endregion


}