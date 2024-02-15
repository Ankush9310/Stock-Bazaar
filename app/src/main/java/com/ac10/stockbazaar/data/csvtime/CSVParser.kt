package com.ac10.stockbazaar.data.csvtime

import java.io.InputStream

interface CSVParser<T> {

    suspend fun parse(inputStream: InputStream): List<T>

}