package com.example.socialnetwork.presentation.register

data class RegisterState(
    val usernameText: String = "",
    val usernameError: UsernameError? = null,
    val emailText: String = "",
    val emailError: EmailError? = null,
    val passwordText: String = "",
    val confirmPasswordText: String = "",
    val passwordError: PasswordError? = null,
    val confirmPasswordError: ConfirmPasswordError? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMsg: Int = 0
) {
    sealed class UsernameError {
        object FieldEmpty : UsernameError()
        object InputTooShort : UsernameError()
        object Invalid : UsernameError()
    }

    sealed class EmailError {
        object FieldEmpty : EmailError()
        object Invalid : EmailError()
    }

    sealed class PasswordError {
        object FieldEmpty : PasswordError()
        object InputTooShort : PasswordError()
        object Invalid : PasswordError()
    }

    sealed class ConfirmPasswordError {
        object FieldEmpty : ConfirmPasswordError()
        object InputTooShort : ConfirmPasswordError()
        object Invalid : ConfirmPasswordError()
        object NotMatch : ConfirmPasswordError()
    }
}
