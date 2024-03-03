package com.aldi.cryptocoins.features.coindetails

import androidx.lifecycle.ViewModel
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.features.coindetails.model.CoinDetails
import com.aldi.cryptocoins.features.coindetails.model.CoinDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CoinDetailsViewModel : ViewModel() {

    private val mockCoinDetails = CoinDetails(
        name = "Ethereum",
        price = "$28.61",
        changePercent = "0.44%",
        changePercentColor = R.color.green,
        marketCap = "$226.94B",
        volume = "$2.46B",
        supply = "120.38M",
    )
    private val mockUiState = CoinDetailsUiState(
        coinDetails = mockCoinDetails,
        isLoading = false,
    )

    private val _uiState = MutableStateFlow(mockUiState)
    val uiState = _uiState.asStateFlow()
}
