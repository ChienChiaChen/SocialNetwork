package com.example.socialnetwork.presentation.create_post

data class CreatePostState(
    val text: String = "",
    val imageUri: String = "",
    val error: Int = 0,
    val isLoading: Boolean = false
)
