package com.ac10.stockbazaar.presentation.company_listings.viewmodel

sealed class CompanyListingsEvent {

    object Refresh : CompanyListingsEvent()

    data class onSearchQueryChange(val query: String) : CompanyListingsEvent()

}