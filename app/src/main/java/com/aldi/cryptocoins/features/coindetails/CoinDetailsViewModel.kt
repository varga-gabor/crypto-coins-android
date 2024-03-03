package com.aldi.cryptocoins.features.coindetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.architecture.navigation.api.Navigator
import com.aldi.cryptocoins.features.coindetails.model.CoinDetails
import com.aldi.cryptocoins.features.coindetails.model.CoinDetailsUiState
import com.aldi.cryptocoins.features.coindetails.navigation.CoinDetailsDestination.Companion.EXTRA_COIN_ID
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase.AutoRefreshResult
import com.aldi.cryptocoins.store.model.Coin
import com.aldi.cryptocoins.util.formatter.CoinNumberFormatter
import com.aldi.cryptocoins.util.formatter.PercentageFormatter
import com.aldi.cryptocoins.util.formatter.PriceFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CoinDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val autoRefreshCoinsUseCase: AutoRefreshCoinsUseCase,
    private val priceFormatter: PriceFormatter,
    private val numberFormatter: CoinNumberFormatter,
    private val percentageFormatter: PercentageFormatter,
    private val navigator: Navigator,
) : ViewModel() {

    private val coinId: String = checkNotNull(savedStateHandle[EXTRA_COIN_ID])

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState = _uiState.asStateFlow()

    suspend fun onViewStarted() {
        autoRefreshCoinsUseCase.coinListFlow
            .mapToUiState()
            .collect { _uiState.value = it }
    }

    private fun Flow<AutoRefreshResult>.mapToUiState(): Flow<CoinDetailsUiState> =
        map { result ->
            when (result) {
                is AutoRefreshResult.Pending -> {
                    _uiState.value.copy(
                        isLoading = true,
                    )
                }

                is AutoRefreshResult.Success -> {
                    CoinDetailsUiState(
                        coinDetails = result.coinList.findCoinById(coinId).toUiModel(),
                        isLoading = false,
                    )
                }
            }
        }

    private fun List<Coin>.findCoinById(coinId: String): Coin =
        first { it.id == coinId }

    private fun Coin.toUiModel(): CoinDetails =
        CoinDetails(
            name = name,
            price = priceFormatter.formatToUsdPrice(priceUsd),
            changePercent = percentageFormatter.format(changePercent24Hr),
            changePercentColor = percentageFormatter.getTextColor(changePercent24Hr),
            marketCap = priceFormatter.formatToUsdPrice(marketCapUsd),
            volume = priceFormatter.formatToUsdPrice(volumeUsd24Hr),
            supply = numberFormatter.abbreviate(supply),
        )

    fun onBackClicked() {
        navigator.navigateBack()
    }

    companion object {
        // This will only be shown after process death (until coin data is fetched)
        private val emptyCoinDetails = CoinDetails(
            name = "",
            price = "",
            changePercent = "",
            changePercentColor = R.color.green,
            marketCap = "",
            volume = "",
            supply = "",
        )
        private val initialUiState = CoinDetailsUiState(
            coinDetails = emptyCoinDetails,
            isLoading = false,
        )
    }
}
