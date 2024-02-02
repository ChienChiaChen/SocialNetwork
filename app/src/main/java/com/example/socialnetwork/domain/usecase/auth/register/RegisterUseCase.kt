package com.example.socialnetwork.domain.usecase.auth.register

import com.example.socialnetwork.common.result.SignUpResult
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.repository.AccountRepository
import com.example.socialnetwork.domain.validator.AuthValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): SignUpResult {

        val emailError = AuthValidator.emailError(email)
        val passwordError = AuthValidator.passwordError(password)
        val confirmPasswordError = AuthValidator.confirmPasswordError(password, confirmPassword)
        val usernameError = AuthValidator.usernameError(username)
        if (emailError != null || usernameError != null || passwordError != null || confirmPasswordError != null) {
            return SignUpResult(emailError, usernameError, passwordError, confirmPasswordError)
        }

        return when (val result =
            withContext(Dispatchers.IO) {
                accountRepository.register(username, email, password)
            }) {
            is DataResult.Success -> SignUpResult(result = DataResult.Success(Unit))
            is DataResult.Error -> SignUpResult(result = DataResult.Error(result.exception))
        }
    }
}
