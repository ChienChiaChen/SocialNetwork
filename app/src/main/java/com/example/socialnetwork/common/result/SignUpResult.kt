package com.example.socialnetwork.common.result

import com.example.socialnetwork.common.error.AuthError
import com.example.socialnetwork.common.wrapper.DataResult


data class SignUpResult(
    val emailError: AuthError? = null,
    val usernameError: AuthError? = null,
    val passwordError: AuthError? = null,
    val confirmPasswordError: AuthError? = null,
    val result: DataResult<Unit>? = null
)
