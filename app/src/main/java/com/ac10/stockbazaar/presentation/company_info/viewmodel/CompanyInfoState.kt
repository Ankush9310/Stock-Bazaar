package com.ac10.stockbazaar.presentation.company_info.viewmodel

import com.ac10.stockbazaar.domain.model.CompanyInfo
import com.ac10.stockbazaar.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfo: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)