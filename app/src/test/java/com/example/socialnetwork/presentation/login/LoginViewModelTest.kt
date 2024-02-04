package com.example.socialnetwork.presentation.login

import app.cash.turbine.test
import com.example.socialnetwork.common.error.AuthError
import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.exception.getStringResId
import com.example.socialnetwork.common.result.LoginResult
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.usecase.auth.login.HasUserUseCase
import com.example.socialnetwork.domain.usecase.auth.login.LoginUseCase
import com.example.socialnetwork.presentation.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
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
class LoginViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    private lateinit var viewModel: LoginViewModel

    @MockK
    private lateinit var loginUseCase: LoginUseCase

    @MockK
    private lateinit var hasUserUseCase: HasUserUseCase

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    @Before
    fun setup() {
        // Turn relaxUnitFun on for all mocks
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = LoginViewModel(
            loginUseCase,
            hasUserUseCase
        )
    }

    //region check user
    @Test
    fun testHasUser() =
        runTest {
            testScope.launch {
                every {
                    hasUserUseCase.invoke()
                } returns true

                // when
                viewModel.onEvent(LoginContract.LoginEvent.HasUser)


                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
                viewModel.effect.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginContract.LoginEffect.HasUser(true), actual)
                    expectNoEvents()
                }
            }
        }

    @Test
    fun testNoHasUser() =
        runTest {
            testScope.launch {
                every {
                    hasUserUseCase.invoke()
                } returns false

                // when
                viewModel.onEvent(LoginContract.LoginEvent.HasUser)


                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
                viewModel.effect.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginContract.LoginEffect.HasUser(false), actual)
                    expectNoEvents()
                }
            }
        }
    //endregion

    @Test
    fun loginSuccessfully() {
        runTest {
            testScope.launch {
                val fakeResult = LoginResult(result = DataResult.Success(Unit))
                coEvery {
                    loginUseCase.invoke(any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(LoginContract.LoginEvent.Login)

                viewModel.effect.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginContract.LoginEffect.NavigateTo, actual)
                    expectNoEvents()
                }
                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun loginFailedWithNetworkIssue() {
        runTest {
            testScope.launch {
                val fakeErrorMsg = NetworkException.NetworkUnavailable.getStringResId()
                val fakeResult =
                    LoginResult(result = DataResult.Error(NetworkException.NetworkUnavailable))
                coEvery {
                    loginUseCase.invoke(any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(LoginContract.LoginEvent.Login)

                viewModel.effect.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginContract.LoginEffect.NavigateTo, actual)
                    expectNoEvents()
                }
                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(fakeErrorMsg, actual.errorMsg)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun loginFailedWithEmailError() {
        runTest {
            testScope.launch {
                val fakeResult = LoginResult(emailError = AuthError.InvalidEmail)
                coEvery {
                    loginUseCase.invoke(any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(LoginContract.LoginEvent.Login)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginState.EmailError.Invalid, actual.emailError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }
    @Test
    fun loginFailedWithEmailEmpty() {
        runTest {
            testScope.launch {
                val fakeResult = LoginResult(emailError = AuthError.EmptyField)
                coEvery {
                    loginUseCase.invoke(any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(LoginContract.LoginEvent.Login)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginState.EmailError.FieldEmpty, actual.emailError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun loginFailedWithPasswordEmpty() {
        runTest {
            testScope.launch {
                val fakeResult = LoginResult(emailError = AuthError.EmptyField)
                coEvery {
                    loginUseCase.invoke(any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(LoginContract.LoginEvent.Login)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginState.PasswordError.FieldEmpty, actual.passwordError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun loginFailedWithPasswordTooShort() {
        runTest {
            testScope.launch {
                val fakeResult = LoginResult(emailError = AuthError.InputTooShort)
                coEvery {
                    loginUseCase.invoke(any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(LoginContract.LoginEvent.Login)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginState.PasswordError.FieldEmpty, actual.passwordError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }


    @Test
    fun loginFailedWithPasswordWithNoUpperCase() {
        runTest {
            testScope.launch {
                val fakeResult = LoginResult(emailError = AuthError.InvalidPassword)
                coEvery {
                    loginUseCase.invoke(any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(LoginContract.LoginEvent.Login)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(LoginState.PasswordError.Invalid, actual.passwordError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }
}