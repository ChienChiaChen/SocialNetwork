package com.example.socialnetwork.presentation.create_post

class CreatePostContract {
    sealed class CreatePostEvent {
        data class EnteredDescription(val value: String) : CreatePostEvent()
        data class EnteredImageUri(val value: String) : CreatePostEvent()
        object Post : CreatePostEvent()

    }
    sealed class CreatePostEffect {
        object NavigateTo : CreatePostEffect()
    }
}
