package com.example.socialnetwork.domain.usecase.auth.login

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.common.result.LoginResult
import com.example.socialnetwork.domain.repository.AccountRepository
import com.example.socialnetwork.domain.validator.AuthValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        val emailError = AuthValidator.emailError(email)
        val pwdError = AuthValidator.passwordError(password)
        if (emailError != null || pwdError != null) {
            return LoginResult(emailError = emailError, passwordError = pwdError)
        }

        return when (val result =
            withContext(Dispatchers.IO) {
                accountRepository.login(email, password)
            }) {
            is DataResult.Success -> LoginResult(result = DataResult.Success(Unit))
            is DataResult.Error -> LoginResult(result = DataResult.Error(result.exception))
        }
    }

}