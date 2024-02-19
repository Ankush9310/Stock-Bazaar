package com.ac10.stockbazaar.di

import com.ac10.stockbazaar.data.csvtime.CSVParser
import com.ac10.stockbazaar.data.csvtime.CompanyListingParser
import com.ac10.stockbazaar.data.csvtime.IntraDayInfoParser
import com.ac10.stockbazaar.data.repository.StockRepositoryImpl
import com.ac10.stockbazaar.domain.model.CompanyListing
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import com.ac10.stockbazaar.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntraDayInfoParser(
        intraDayInfoParser: IntraDayInfoParser
    ): CSVParser<IntraDayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository


}