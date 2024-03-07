package com.ac10.stockbazaar.data.repository

import app.cash.turbine.test
import com.ac10.stockbazaar.data.csvtime.CSVParser
import com.ac10.stockbazaar.data.local.CompanyListingEntity
import com.ac10.stockbazaar.data.local.StockDao
import com.ac10.stockbazaar.data.local.StockDaoFake
import com.ac10.stockbazaar.data.local.StockDatabase
import com.ac10.stockbazaar.data.mappers.toCompanyListing
import com.ac10.stockbazaar.data.remote.StockApi
import com.ac10.stockbazaar.domain.model.CompanyListing
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import com.ac10.stockbazaar.util.MyResult
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class StockRepositoryImplTest {

    private val companyListings = (1..100).map {
        CompanyListing(
            name = "name$it",
            symbol = "symbol$it",
            exchange = "exchange$it"
        )
    }

    private val intraDayInfos = (1..100).map {
        IntraDayInfo(
            date = LocalDateTime.now(),
            close = it.toDouble()
        )
    }

    private lateinit var repository: StockRepositoryImpl
    private lateinit var api: StockApi
    private lateinit var db: StockDatabase
    private lateinit var dao: StockDao
    private lateinit var companyListingsParser: CSVParser<CompanyListing>
    private lateinit var intradayInfoParser: CSVParser<IntraDayInfo>

    @Before
    fun setUp() {
        api = mockk(relaxed = true) {
            coEvery { getListings(any()) } returns mockk(relaxed = true)
        }
        dao = StockDaoFake()
        db = mockk(relaxed = true) {
            every { stockDao } returns dao
        }
        companyListingsParser = mockk(relaxed = true) {
            coEvery { parse(any()) } returns companyListings
        }
        intradayInfoParser = mockk(relaxed = true) {
            coEvery { parse(any()) } returns intraDayInfos
        }
        repository = StockRepositoryImpl(
            api = api,
            db = db,
            companyListingParser = companyListingsParser,
            intraDayInfoParser = intradayInfoParser
        )
    }

    @Test
    fun `Test local database cache with fetch from remote set to true`() = runTest {
        val localListings = listOf(
            CompanyListingEntity(
                name = "test-name",
                symbol = "test-symbol",
                exchange = "test-exchange",
                id = 0
            )
        )
        dao.insertCompanyListings(localListings)

        repository.getCompanyListings(
            fetchFromRemote = true,
            query = ""
        ).test {
            val startLoading = awaitItem()
            assertThat((startLoading as MyResult.Loading).isLoading).isTrue()

            val listingsFromDb = awaitItem()
            Truth.assertThat(listingsFromDb is MyResult.Success).isTrue()
            assertThat(listingsFromDb.data).isEqualTo(localListings.map { it.toCompanyListing() })

            val remoteListingsFromDb = awaitItem()
            Truth.assertThat(remoteListingsFromDb is MyResult.Success).isTrue()
            assertThat(remoteListingsFromDb.data).isEqualTo(
                dao.searchCompanyListing("").map { it.toCompanyListing() }
            )

            val stopLoading = awaitItem()
            assertThat((stopLoading as MyResult.Loading).isLoading).isFalse()

            awaitComplete()
        }


    }

}