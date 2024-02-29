package com.ac10.stockbazaar.data.repository

import com.ac10.stockbazaar.domain.model.CompanyInfo
import com.ac10.stockbazaar.domain.model.CompanyListing
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import com.ac10.stockbazaar.domain.repository.StockRepository
import com.ac10.stockbazaar.util.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class StockRepositoryFake : StockRepository {

    var companyListingsToReturn = (1..10).map {
        CompanyListing(
            name = "name$it",
            symbol = "symbol$it",
            exchange = "exchange$it",
        )
    }

    var intraDayListingsToReturn = (1..10).map {
        IntraDayInfo (
            date = LocalDateTime.now(),
            close = it.toDouble()
        )
    }

    var companyInfoToReturn = CompanyInfo(
        symbol = "symbol",
        description = "description",
        name = "name",
        country = "country",
        industry = "industry"
    )


    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<MyResult<List<CompanyListing>>> {
        return flow {
            emit(
                MyResult.Success(
                    companyListingsToReturn
                )
            )
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): MyResult<List<IntraDayInfo>> {
        return MyResult.Success(intraDayListingsToReturn)
    }

    override suspend fun getCompanyInfo(symbol: String): MyResult<CompanyInfo> {
        return MyResult.Success(companyInfoToReturn)
    }
}