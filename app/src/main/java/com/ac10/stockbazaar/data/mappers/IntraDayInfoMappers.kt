package com.ac10.stockbazaar.data.mappers

import com.ac10.stockbazaar.data.remote.dto.IntraDayInfoDto
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntraDayInfoDto.toIntraDatInfo() : IntraDayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(pattern, formatter)

    return IntraDayInfo(
        date = localDateTime,
        close = close
    )



}