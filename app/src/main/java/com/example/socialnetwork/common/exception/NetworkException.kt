package com.example.socialnetwork.common.exception

sealed class NetworkException : Exception() {
    object NetworkUnavailable : NetworkException()
}
