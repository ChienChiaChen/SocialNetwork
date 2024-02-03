package com.example.socialnetwork.domain.repository

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post

interface PostRepository {
    suspend fun createPost(description: String, imageUri: String): DataResult<Unit>

    suspend fun fetchCurrentUserPost(): DataResult<List<Post>>

}