package com.example.socialnetwork.presentation.main_feed

class MainFeedContract {
    sealed class MainFeedEvent {
        object RefreshPost : MainFeedEvent()
    }

}