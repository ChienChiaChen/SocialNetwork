package com.example.socialnetwork.domain.validator

import android.util.Patterns
import com.example.socialnetwork.common.Constants
import com.example.socialnetwork.common.error.AuthError


object AuthValidator {
    fun emailError(email: String): AuthError? {
        return when {
            email.isEmpty() -> AuthError.EmptyField
            !isValidEmail(email) -> AuthError.InvalidEmail
            else -> null
        }
    }

    fun usernameError(username: String): AuthError? {
        return when {
            username.isEmpty() -> AuthError.EmptyField
            username.count() < Constants.MIN_USERNAME_LENGTH -> AuthError.InputTooShort
            !username.isAlphaNumeric() -> AuthError.InvalidUsername
            else -> null
        }
    }

    fun passwordError(password: String): AuthError? {
        return when {
            password.isEmpty() -> AuthError.EmptyField
            !isValidPassword(password) -> AuthError.InvalidPassword
            else -> null
        }
    }

    fun confirmPasswordError(password: String, confirmPassword: String): AuthError? {
        return when {
            confirmPassword.isEmpty() -> AuthError.EmptyField
            !isValidPassword(confirmPassword) -> AuthError.InvalidPassword
            !passwordMatches(
                password,
                confirmPassword
            ) -> AuthError.UnmatchedPassword

            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun String.isAlphaNumeric() = matches("[a-zA-Z0-9]+".toRegex())

    private fun isValidPassword(password: String): Boolean =
        password.count() >= Constants.MIN_PASSWORD_LENGTH

    private fun passwordMatches(newPassword: String, confirmNewPassword: String): Boolean =
        newPassword == confirmNewPassword
}
