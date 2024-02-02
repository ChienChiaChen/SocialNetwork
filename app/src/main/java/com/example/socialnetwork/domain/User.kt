package com.example.socialnetwork.domain

data class User(
    val username: String,
    val email:String,
    val profilePictureUrl: String = "",
    val description: String? = "",
)
