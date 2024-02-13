package com.ac10.stockbazaar.util


sealed class Result<T>(val data: T? = null, message: String? = null) {

    class Success<T>(data: T?) : Result<T>(data)

    class Error<T>(data: T? = null, message: String?) : Result<T>(data, message)

    class Loading<T>(val isLoading: Boolean = true) : Result<T>(null)
}