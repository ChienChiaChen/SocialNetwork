package com.example.socialnetwork.presentation.profile

class ProfileContract {
    sealed class ProfileEvent {
        object RefreshPost : ProfileEvent()
        object RefreshUserInfo : ProfileEvent()

        data class EnteredImageUri(val value: String) : ProfileEvent()

    }

}