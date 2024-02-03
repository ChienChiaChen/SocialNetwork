package com.example.socialnetwork.domain

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post(
    val uid: String = "",
    val username: String = "",
    val imageUrl: String = "",
    val profilePictureUrl: String = "",
    val description: String = "",
    @ServerTimestamp val createdAt: Date = Date(),
    @DocumentId val id: String = "",
)
