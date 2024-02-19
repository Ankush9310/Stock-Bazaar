package com.ac10.stockbazaar.presentation.company_info.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ac10.stockbazaar.domain.repository.StockRepository
import com.ac10.stockbazaar.util.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(
                isLoading = true
            )
            val companyInfoResult = async {
                repository.getCompanyInfo(symbol)
            }
            val intraDayInfoResult = async {
                repository.getIntraDayInfo(symbol)
            }
            when (val result = companyInfoResult.await()) {
                is MyResult.Success -> {
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }

                is MyResult.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )
                }

                else -> Unit
            }

            when (val result = intraDayInfoResult.await()) {
                is MyResult.Success -> {
                    state = state.copy(
                        stockInfo = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }

                is MyResult.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null,
                    )
                }

                else -> Unit

            }
        }
    }


}