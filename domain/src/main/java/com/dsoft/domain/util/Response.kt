package com.dsoft.domain.util

sealed class Response<T>(val data: T?, open val exception: Exception? = null) {
    class Loading<T> : Response<T>(null)
    class Success<T>(data: T) : Response<T>(data)
    class Failure<T>(override val exception: Exception): Response<T>(null, exception)
}