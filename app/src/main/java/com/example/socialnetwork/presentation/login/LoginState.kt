package com.example.socialnetwork.presentation.login

data class LoginState(
    val emailText: String = "",
    val emailError: EmailError? = null,
    val passwordText: String = "",
    val passwordError: PasswordError? = null,
    val isPasswordVisible: Boolean = false,
    val hasUser:Boolean = false,
    val isLoading:Boolean = true,
    val errorMsg: Int = 0
) {
    sealed class EmailError {
        object FieldEmpty : EmailError()
        object Invalid : EmailError()
    }

    sealed class PasswordError {
        object FieldEmpty : PasswordError()
        object InputTooShort : PasswordError()
        object Invalid : PasswordError()
    }
}
