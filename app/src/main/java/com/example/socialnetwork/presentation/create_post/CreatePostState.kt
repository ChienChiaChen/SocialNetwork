package com.example.socialnetwork.presentation.create_post

data class CreatePostState(
    val text: String = "",
    val imageUri: String = "",
    val isLoading: Boolean = false
)
