package com.ac10.stockbazaar.data.repository

import com.ac10.stockbazaar.data.csvtime.CSVParser
import com.ac10.stockbazaar.data.local.StockDatabase
import com.ac10.stockbazaar.data.mappers.toCompanyInfo
import com.ac10.stockbazaar.data.mappers.toCompanyListing
import com.ac10.stockbazaar.data.mappers.toCompanyListingEntity
import com.ac10.stockbazaar.data.remote.StockApi
import com.ac10.stockbazaar.domain.model.CompanyInfo
import com.ac10.stockbazaar.domain.model.CompanyListing
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import com.ac10.stockbazaar.domain.repository.StockRepository
import com.ac10.stockbazaar.util.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intraDayInfoParser: CSVParser<IntraDayInfo>,
) : StockRepository {

    private val stockDao = db.stockDao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<MyResult<List<CompanyListing>>> {
        return flow {
            emit(MyResult.Loading(true))

            val localListings = stockDao.searchCompanyListing(query)
            emit(MyResult.Success(
                data = localListings.map {
                    it.toCompanyListing()
                }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if (loadFromCache) {
                emit(MyResult.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (exception: IOException) {
                exception.printStackTrace()
                emit(
                    MyResult.Error(
                        message = "Couldn't load the data"
                    )
                )
                null
            } catch (exception: HttpException) {
                exception.printStackTrace()
                emit(
                    MyResult.Error(
                        message = "Couldn't load the data"
                    )
                )
                null
            }

            remoteListing?.let { listings ->
                stockDao.clearCompanyListings()
                stockDao.insertCompanyListings(
                    listings.map {
                        it.toCompanyListingEntity()
                    }
                )

                emit(MyResult.Success(
                    data = stockDao
                        .searchCompanyListing("")
                        .map {
                            it.toCompanyListing()
                        }
                ))
                emit(MyResult.Loading(false))

            }

        }
    }


    override suspend fun getIntraDayInfo(symbol: String): MyResult<List<IntraDayInfo>> {
        return try {
            val response = api.getIntraDayInfo(symbol)

            val results = intraDayInfoParser.parse(response.byteStream())
            MyResult.Success(results)

        } catch (exception: IOException) {
            exception.printStackTrace()
            MyResult.Error(
                message = "Couldn't load IntraDay Info"
            )
        } catch (exception: HttpException) {
            exception.printStackTrace()
            MyResult.Error(
                message = "Couldn't load IntraDay Info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): MyResult<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            MyResult.Success(result.toCompanyInfo())

        } catch (exception: IOException) {
            exception.printStackTrace()
            MyResult.Error(
                message = "Couldn't load company info"
            )
        } catch (exception: HttpException) {
            exception.printStackTrace()
            MyResult.Error(
                message = "Couldn't load company info"
            )
        }
    }
}