package com.example.socialnetwork.domain.repository

import com.example.socialnetwork.common.wrapper.DataResult

interface AccountRepository {
    val currentUserId: String
    val hasUser: Boolean

    suspend fun register(email: String, password: String): DataResult<Unit>
    suspend fun login(email: String, password: String): DataResult<Unit>
    suspend fun logout(): DataResult<Unit>
}