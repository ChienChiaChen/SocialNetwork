package com.example.socialnetwork.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.common.SingleSharedFlow
import com.example.socialnetwork.common.error.toConfirmPasswordErrorState
import com.example.socialnetwork.common.error.toEmailErrorState
import com.example.socialnetwork.common.error.toPasswordErrorState
import com.example.socialnetwork.common.error.toUserNameErrorState
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.usecase.auth.register.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private val _effect = SingleSharedFlow<RegisterContract.RegisterEffect>()
    val effect: SharedFlow<RegisterContract.RegisterEffect> = _effect

    fun onEvent(event: RegisterContract.RegisterEvent) {
        when (event) {
            is RegisterContract.RegisterEvent.EnteredUsername ->
                _state.value = _state.value.copy(usernameText = event.value)

            is RegisterContract.RegisterEvent.EnteredEmail ->
                _state.value = _state.value.copy(emailText = event.value)

            is RegisterContract.RegisterEvent.EnteredPassword ->
                _state.value = _state.value.copy(passwordText = event.value)

            is RegisterContract.RegisterEvent.EnteredConfirmPassword ->
                _state.value = _state.value.copy(confirmPasswordText = event.value)

            is RegisterContract.RegisterEvent.TogglePasswordVisibility ->
                _state.value =
                    _state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)

            is RegisterContract.RegisterEvent.Register -> {
                _state.value = _state.value.copy(isLoading = true)
                viewModelScope.launch {
                    val tempState = state.value
                    val signUpResult = registerUseCase.invoke(
                        tempState.emailText,
                        tempState.usernameText,
                        tempState.passwordText,
                        tempState.confirmPasswordText
                    )

                    _state.value = _state.value.copy(
                        emailError = signUpResult.emailError?.toEmailErrorState(),
                        usernameError = signUpResult.usernameError?.toUserNameErrorState(),
                        passwordError = signUpResult.passwordError?.toPasswordErrorState(),
                        confirmPasswordError = signUpResult.confirmPasswordError?.toConfirmPasswordErrorState()
                    )
                    _state.value = _state.value.copy(isLoading = false)
                    when (signUpResult.result) {
                        is DataResult.Success<*> -> {
                            _effect.tryEmit(RegisterContract.RegisterEffect.NavigateTo)
                        }

                        is DataResult.Error<*> -> {
                            // send Error msg
                        }

                        null -> return@launch
                    }

                }
            }
        }
    }
}