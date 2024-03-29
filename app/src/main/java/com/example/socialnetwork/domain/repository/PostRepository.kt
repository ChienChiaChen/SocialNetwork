package com.example.socialnetwork.domain.repository

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post
import com.google.firebase.firestore.CollectionReference

interface PostRepository {
    val post: CollectionReference
    suspend fun createPost(description: String, imageUri: String): DataResult<Unit>

    suspend fun fetchCurrentUserPost(): DataResult<List<Post>>
    suspend fun fetchAllUserPost(): DataResult<List<Post>>

}