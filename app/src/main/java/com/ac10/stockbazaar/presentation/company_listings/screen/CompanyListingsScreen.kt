package com.ac10.stockbazaar.presentation.company_listings.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ac10.stockbazaar.presentation.company_listings.viewmodel.CompanyListingsEvent
import com.ac10.stockbazaar.presentation.company_listings.viewmodel.CompanyListingsViewModel
import com.ac10.stockbazaar.presentation.destinations.CompanyInfoScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingsViewModel = hiltViewModel()
) {

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    CompanyListingsEvent.onSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Search..."
                )
            },
            maxLines = 1,
            singleLine = true
        )

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(
                    CompanyListingsEvent.Refresh
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    state.companies.size
                ) { item ->
                    val company = state.companies[item]

                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    CompanyInfoScreenDestination(
                                        symbol = company.symbol
                                    )
                                )
                            }
                            .padding(16.dp)
                    )

                    if (item < state.companies.size) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(
                                    horizontal = 16.dp
                                )
                        )
                    }


                }
            }

        }

    }

}