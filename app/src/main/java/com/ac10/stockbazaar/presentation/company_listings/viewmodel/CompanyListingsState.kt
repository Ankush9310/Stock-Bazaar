package com.ac10.stockbazaar.presentation.company_listings.viewmodel

import com.ac10.stockbazaar.domain.model.CompanyListing

data class CompanyListingsState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "0",

    )