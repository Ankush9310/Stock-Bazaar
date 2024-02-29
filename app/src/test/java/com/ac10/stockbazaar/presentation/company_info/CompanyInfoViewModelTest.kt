package com.ac10.stockbazaar.presentation.company_info

import androidx.lifecycle.SavedStateHandle
import com.ac10.stockbazaar.MainCoroutineRule
import com.ac10.stockbazaar.data.repository.StockRepositoryFake
import com.ac10.stockbazaar.domain.model.CompanyInfo
import com.ac10.stockbazaar.domain.model.IntraDayInfo
import com.ac10.stockbazaar.presentation.company_info.viewmodel.CompanyInfoViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class CompanyInfoViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CompanyInfoViewModel
    private lateinit var repositoryFake: StockRepositoryFake

    @Before
    fun setUp() {
        repositoryFake = StockRepositoryFake()
        viewModel = CompanyInfoViewModel(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "symbol" to "AMAZO"
                )
            ),
            repository = repositoryFake
        )
    }

    @Test
    fun `Company and intraDay info are properly mapped to state`() {
        val companyInfo = CompanyInfo(
            symbol = "AMAZO",
            description = "Amazon desc",
            name = "Amazon",
            country = "USA",
            industry = "E-Commerce"
        )
        repositoryFake.companyInfoToReturn = companyInfo

        val intraDayInfos = listOf(
            IntraDayInfo(
                date = LocalDateTime.now(),
                close = 10.0
            )
        )

        repositoryFake.intraDayListingsToReturn = intraDayInfos

        coroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.company).isEqualTo(companyInfo)
        assertThat(viewModel.state.stockInfo).isEqualTo(intraDayInfos)
    }


}