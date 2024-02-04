package com.example.socialnetwork.presentation.profile

import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.User

data class ProfileDataState(
    val user: User = User(),
    val post: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val isUserInfoLoading: Boolean = false,
    val errorMsg: Int = 0
)
