package com.ac10.stockbazaar.data.csvtime

import com.ac10.stockbazaar.data.mappers.toIntraDatInfo
import com.ac10.stockbazaar.data.remote.dto.IntraDayInfoDto
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntraDayInfoParser @Inject constructor() : CSVParser<IntraDayInfo> {

    override suspend fun parse(inputStream: InputStream): List<IntraDayInfo> {
        val csvReader = CSVReader(InputStreamReader(inputStream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timeStamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(1) ?: return@mapNotNull null

                    val dto = IntraDayInfoDto(timeStamp, close.toDouble())
                    dto.toIntraDatInfo()
                }
                .filter {
                    it.date.dayOfMonth == LocalDate.now().minusDays(4).dayOfMonth
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}