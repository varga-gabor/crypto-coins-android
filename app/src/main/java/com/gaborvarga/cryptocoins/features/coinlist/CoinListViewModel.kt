package com.aldi.cryptocoins.features.coinlist

import androidx.lifecycle.ViewModel
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.features.coinlist.model.CoinListEntry
import com.aldi.cryptocoins.features.coinlist.model.CoinListUiState
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CoinListViewModel(
    private val autoRefreshCoinsUseCase: AutoRefreshCoinsUseCase,
) : ViewModel() {

    suspend fun onViewStarted() {
        autoRefreshCoinsUseCase.coinListFlow
            .map { _ ->
                CoinListUiState(
                    coinList = mockCoinList,
                    isLoading = false,
                )
            }
            .collect { _uiState.value = it }
    }

    private val mockCoinList = listOf(
        CoinListEntry(
            name = "Bitcoin",
            symbol = "BTC",
            price = "$1.00K",
            changePercent = "-0.01%",
            icon = R.drawable.ic_bitcoin,
            changePercentColor = R.color.red,
        ),
        CoinListEntry(
            name = "Ethereum",
            symbol = "ETH",
            price = "$1.28K",
            changePercent = "0.44%",
            icon = R.drawable.ic_ethereum,
            changePercentColor = R.color.green,
        ),
    )
    private val mockUiState = CoinListUiState(
        coinList = mockCoinList,
        isLoading = false,
    )

    private val _uiState = MutableStateFlow(mockUiState)
    val uiState = _uiState.asStateFlow()
}
