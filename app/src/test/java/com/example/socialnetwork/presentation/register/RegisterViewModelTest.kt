package com.example.socialnetwork.presentation.register

import app.cash.turbine.test
import com.example.socialnetwork.common.error.AuthError
import com.example.socialnetwork.common.result.SignUpResult
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.usecase.auth.register.RegisterUseCase
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
class RegisterViewModelTest {
    private lateinit var viewModel: RegisterViewModel

    @MockK
    private lateinit var registerUseCase: RegisterUseCase


    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)


    @Before
    fun setup() {
        // Turn relaxUnitFun on for all mocks
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = RegisterViewModel(registerUseCase)
    }

    @Test
    fun registerSuccessfully() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(result = DataResult.Success(Unit))
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                RegisterContract.RegisterEvent.Register

                viewModel.effect.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterContract.RegisterEffect.NavigateTo, actual)
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
    fun registerFailedWithEmailEmptyField() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(emailError = AuthError.EmptyField)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.EmailError.FieldEmpty, actual.emailError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun registerFailedWithEmailInvalidEmail() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(emailError = AuthError.InvalidEmail)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.EmailError.Invalid, actual.emailError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun registerFailedWithUserNameEmptyField() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(usernameError = AuthError.EmptyField)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.UsernameError.FieldEmpty, actual.usernameError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun registerFailedWithUserNameInputTooShort() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(usernameError = AuthError.InputTooShort)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.UsernameError.InputTooShort, actual.usernameError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun registerFailedWithInvalidUsername() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(usernameError = AuthError.InvalidUsername)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.UsernameError.Invalid, actual.usernameError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun registerFailedWithPwdEmptyField() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(passwordError = AuthError.EmptyField)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.PasswordError.FieldEmpty, actual.passwordError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun registerFailedWithPwdInputTooShort() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(passwordError = AuthError.InputTooShort)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.PasswordError.InputTooShort, actual.passwordError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun registerFailedWithInvalidPassword() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(passwordError = AuthError.InvalidPassword)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.PasswordError.Invalid, actual.passwordError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }


    @Test
    fun registerFailedWithUnmatchedPassword() {
        runTest {
            testScope.launch {
                val fakeResult = SignUpResult(passwordError = AuthError.UnmatchedPassword)
                coEvery {
                    registerUseCase.invoke(any(), any(), any(), any())
                } returns fakeResult

                // when
                viewModel.onEvent(RegisterContract.RegisterEvent.Register)

                viewModel.state.test {
                    val actual = awaitItem()
                    Assert.assertEquals(RegisterState.ConfirmPasswordError.NotMatch, actual.confirmPasswordError)
                    Assert.assertEquals(false, actual.isLoading)
                    expectNoEvents()
                }
            }
        }
    }
}