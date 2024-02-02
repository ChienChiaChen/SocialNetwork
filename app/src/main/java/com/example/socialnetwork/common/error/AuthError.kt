package com.example.socialnetwork.common.error

import com.example.socialnetwork.presentation.register.RegisterState

sealed class AuthError : Error() {
    object EmptyField : AuthError()
    object InputTooShort : AuthError()
    object InvalidEmail : AuthError()
    object InvalidUsername : AuthError()
    object InvalidPassword : AuthError()
    object UnmatchedPassword : AuthError()
}

fun AuthError?.toEmailErrorState(): RegisterState.EmailError? {
    return when (this) {
        AuthError.EmptyField -> RegisterState.EmailError.FieldEmpty
        null -> null
        else -> RegisterState.EmailError.Invalid
    }
}

fun AuthError?.toUserNameErrorState(): RegisterState.UsernameError? {
    return when (this) {
        AuthError.EmptyField -> RegisterState.UsernameError.FieldEmpty
        AuthError.InputTooShort -> RegisterState.UsernameError.InputTooShort
        null -> null
        else -> RegisterState.UsernameError.Invalid
    }
}

fun AuthError?.toPasswordErrorState(): RegisterState.PasswordError? {
    return when (this) {
        AuthError.EmptyField -> RegisterState.PasswordError.FieldEmpty
        AuthError.InputTooShort -> RegisterState.PasswordError.InputTooShort
        null -> null
        else -> RegisterState.PasswordError.Invalid
    }
}

fun AuthError?.toConfirmPasswordErrorState(): RegisterState.ConfirmPasswordError? {
    return when (this) {
        AuthError.EmptyField -> RegisterState.ConfirmPasswordError.FieldEmpty
        AuthError.InputTooShort -> RegisterState.ConfirmPasswordError.InputTooShort
        AuthError.UnmatchedPassword -> RegisterState.ConfirmPasswordError.NotMatch
        null -> null
        else -> RegisterState.ConfirmPasswordError.Invalid
    }
}


