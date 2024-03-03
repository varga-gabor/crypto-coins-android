package com.aldi.cryptocoins.features.coinlist

import androidx.lifecycle.ViewModel
import com.aldi.cryptocoins.R.color
import com.aldi.cryptocoins.R.drawable
import com.aldi.cryptocoins.features.coinlist.model.CoinListEntry
import com.aldi.cryptocoins.features.coinlist.model.CoinListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CoinListViewModel : ViewModel() {

    private val mockCoinList = listOf(
        CoinListEntry(
            name = "Bitcoin",
            symbol = "BTC",
            price = "$1.00K",
            changePercent = "-0.01%",
            icon = drawable.ic_bitcoin,
            changePercentColor = color.red,
        ),
        CoinListEntry(
            name = "Ethereum",
            symbol = "ETH",
            price = "$1.28K",
            changePercent = "0.44%",
            icon = drawable.ic_ethereum,
            changePercentColor = color.green,
        ),
    )
    private val mockUiState = CoinListUiState(
        coinList = mockCoinList,
        isLoading = false,
    )

    private val _uiState = MutableStateFlow(mockUiState)
    val uiState = _uiState.asStateFlow()
}
