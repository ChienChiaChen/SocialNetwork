package com.example.socialnetwork.domain.usecase.auth.login

import com.example.socialnetwork.domain.repository.AccountRepository
import javax.inject.Inject

class HasUserUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Boolean = accountRepository.hasUser
}