package com.ac10.stockbazaar.domain.repository

import com.ac10.stockbazaar.domain.model.CompanyInfo
import com.ac10.stockbazaar.domain.model.CompanyListing
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Result<List<CompanyListing>>>


    suspend fun getIntraDayInfo(
        symbol: String
    ): Result<List<IntraDayInfo>>


    suspend fun getCompanyInfo(
        symbol: String
    ): Result<CompanyInfo>

}