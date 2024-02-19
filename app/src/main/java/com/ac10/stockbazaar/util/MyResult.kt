package com.ac10.stockbazaar.util


sealed class MyResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T?) : MyResult<T>(data)

    class Error<T>(message: String?, data: T? = null) : MyResult<T>(data, message)

    class Loading<T>(val isLoading: Boolean = true) : MyResult<T>(null)
}