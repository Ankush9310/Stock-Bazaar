package com.ac10.stockbazaar.domain.repository

import com.ac10.stockbazaar.domain.model.CompanyInfo
import com.ac10.stockbazaar.domain.model.CompanyListing
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import com.ac10.stockbazaar.util.MyResult
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<MyResult<List<CompanyListing>>>


    suspend fun getIntraDayInfo(
        symbol: String
    ): MyResult<List<IntraDayInfo>>


    suspend fun getCompanyInfo(
        symbol: String
    ): MyResult<CompanyInfo>

}