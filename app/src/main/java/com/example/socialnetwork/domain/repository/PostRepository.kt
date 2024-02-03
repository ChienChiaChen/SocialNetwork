package com.example.socialnetwork.domain.repository

import com.example.socialnetwork.common.wrapper.DataResult

interface PostRepository {
    suspend fun createPost(description: String, imageUri: String): DataResult<Unit>
}