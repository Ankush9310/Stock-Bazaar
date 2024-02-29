package com.ac10.stockbazaar.data.local

class StockDaoFake : StockDao {

    private var companyListings = emptyList<CompanyListingEntity>()

    override suspend fun insertCompanyListings(companyListingEntity: List<CompanyListingEntity>) {
        companyListings = companyListings + companyListingEntity
    }

    override suspend fun searchCompanyListing(query: String): List<CompanyListingEntity> {
        return companyListings.filter {
            it.name.lowercase().contains(query.lowercase())
        }
    }

    override suspend fun clearCompanyListings() {
        companyListings = emptyList()
    }
}