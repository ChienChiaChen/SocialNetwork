package com.example.socialnetwork.common.exception

sealed class NetworkException : Exception() {
    object NetworkUnavailable : NetworkException()
    object NotAuthorized : NetworkException()
    object NoData : NetworkException()
    object BadRequest : NetworkException()

}
