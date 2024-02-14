package com.example.socialnetwork.common.exception

import com.example.socialnetwork.R

fun Throwable.getStringResId(): Int {
    return when (this) {
        is NetworkException.NetworkUnavailable -> R.string.error_network_unavailable_message
        is NetworkException.NotFound -> R.string.error_not_found_data_message
        is NetworkException.BadRequest -> R.string.error_unknown_network_error_message
        is NetworkException.NotAuthorized -> R.string.error_invalid_credentials_message
        else -> UNREAD_ERROR_CODE
    }
}

const val UNREAD_ERROR_CODE = 0
