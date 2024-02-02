package com.example.socialnetwork.common.result

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.common.error.AuthError


data class LoginResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: DataResult<Unit>? = null
)
