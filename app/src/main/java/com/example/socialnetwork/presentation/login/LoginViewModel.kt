package com.example.socialnetwork.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.common.SingleSharedFlow
import com.example.socialnetwork.common.error.toLoginEmailErrorState
import com.example.socialnetwork.common.error.toLoginPasswordErrorState
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.usecase.auth.login.HasUserUseCase
import com.example.socialnetwork.domain.usecase.auth.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val hasUserUseCase: HasUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _effect = SingleSharedFlow<LoginContract.LoginEffect>()
    val effect: SharedFlow<LoginContract.LoginEffect> = _effect

    fun onEvent(event: LoginContract.LoginEvent) {
        when (event) {
            is LoginContract.LoginEvent.HasUser -> {
                _state.value = _state.value.copy(isLoading = true)
                val hasUser = hasUserUseCase.invoke()
                _effect.tryEmit(LoginContract.LoginEffect.HasUser(hasUser))
                _state.value = _state.value.copy(isLoading = false)
            }

            is LoginContract.LoginEvent.EnteredEmail ->
                _state.value = _state.value.copy(emailText = event.value)

            is LoginContract.LoginEvent.EnteredPassword ->
                _state.value = _state.value.copy(passwordText = event.value)

            is LoginContract.LoginEvent.TogglePasswordVisibility ->
                _state.value =
                    _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)

            is LoginContract.LoginEvent.Login -> {
                _state.value = _state.value.copy(isLoading = true)
                viewModelScope.launch {
                    val tempState = state.value
                    val loginResult = loginUseCase(tempState.emailText, tempState.passwordText)

                    _state.value = _state.value.copy(
                        emailError = loginResult.emailError?.toLoginEmailErrorState(),
                        passwordError = loginResult.emailError?.toLoginPasswordErrorState()
                    )

                    when (loginResult.result) {
                        is DataResult.Success<*> -> {
                            _effect.tryEmit(LoginContract.LoginEffect.NavigateTo)
                        }

                        is DataResult.Error<*> -> {
                            // send Error msg
                        }

                        null -> return@launch
                    }
                }
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}