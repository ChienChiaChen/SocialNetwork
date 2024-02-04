package com.example.socialnetwork.common.exception

sealed class NetworkException : Exception() {
    object NetworkUnavailable : NetworkException()
    object NotAuthorized : NetworkException()
    object NotFound : NetworkException()
    object BadRequest : NetworkException()

}
