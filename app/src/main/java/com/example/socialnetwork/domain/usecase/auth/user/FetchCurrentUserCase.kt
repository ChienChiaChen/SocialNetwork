package com.example.socialnetwork.domain.usecase.auth.user

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.User
import com.example.socialnetwork.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchCurrentUserCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): DataResult<User> {
        return when (val result = withContext(Dispatchers.IO) {
            accountRepository.fetchCurrentUser()
        }) {
            is DataResult.Success -> {
                DataResult.Success(result.data)
            }

            is DataResult.Error -> result
        }
    }
}