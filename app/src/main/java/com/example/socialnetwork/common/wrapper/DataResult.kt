package com.example.socialnetwork.common.wrapper

sealed class DataResult<out T> {
    class Success<T>(val data: T) : DataResult<T>()
    data class Error<T>(val exception: Exception) : DataResult<T>()
}

internal inline fun <T> getResult(block: () -> T): DataResult<T> = try {
    block().let { DataResult.Success(it) }
} catch (e: Exception) {
    DataResult.Error(e)
}