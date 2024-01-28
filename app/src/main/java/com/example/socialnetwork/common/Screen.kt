package com.example.socialnetwork.common

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object MainFeedScreen : Screen("main_feed_screen")
    object ProfileScreen : Screen("profile_screen")
    object CreatePostScreen : Screen("create_post_screen")
}
