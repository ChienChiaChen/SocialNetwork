package com.example.socialnetwork.domain.repository

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.User

interface AccountRepository {
    val currentUserId: String
    val hasUser: Boolean

    suspend fun register(username:String, email: String, password: String): DataResult<Unit>
    suspend fun login(email: String, password: String): DataResult<Unit>
    suspend fun logout(): DataResult<Unit>

    suspend fun fetchCurrentUser(): DataResult<User>
    suspend fun updateCurrentUser(profilePictureUrl: String): DataResult<User>
}