package com.example.socialnetwork.presentation.main_feed

import com.example.socialnetwork.domain.Post

data class MainFeedDataState(
    val post: List<Post> = emptyList(),
    val isLoading: Boolean = true
)
