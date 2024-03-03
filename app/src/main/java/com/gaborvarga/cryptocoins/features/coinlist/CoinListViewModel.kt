package com.aldi.cryptocoins.features.coinlist

import androidx.lifecycle.ViewModel
import com.aldi.cryptocoins.features.coinlist.model.CoinListEntry
import com.aldi.cryptocoins.features.coinlist.model.CoinListUiState
import com.aldi.cryptocoins.formatter.PercentageFormatter
import com.aldi.cryptocoins.formatter.PriceFormatter
import com.aldi.cryptocoins.model.Coin
import com.aldi.cryptocoins.resourceprovider.CoinResourceProvider
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase.AutoRefreshResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CoinListViewModel(
    private val autoRefreshCoinsUseCase: AutoRefreshCoinsUseCase,
    private val priceFormatter: PriceFormatter,
    private val percentageFormatter: PercentageFormatter,
    private val resourceProvider: CoinResourceProvider,
) : ViewModel() {

    suspend fun onViewStarted() {
        autoRefreshCoinsUseCase.coinListFlow
            .mapToUiState()
            .collect { _uiState.value = it }
    }

    private fun Flow<AutoRefreshResult>.mapToUiState(): Flow<CoinListUiState> =
        map { result ->
            when (result) {
                is AutoRefreshResult.Pending -> {
                    CoinListUiState(
                        coinList = emptyList(),
                        isLoading = true,
                    )
                }

                is AutoRefreshResult.Success -> {
                    CoinListUiState(
                        coinList = result.coinList.mapToUiModel(),
                        isLoading = false,
                    )
                }
            }
        }

    private fun List<Coin>.mapToUiModel(): List<CoinListEntry> =
        map { coin ->
            CoinListEntry(
                name = coin.name,
                symbol = coin.symbol,
                price = priceFormatter.formatToUsdPrice(coin.price),
                changePercent = percentageFormatter.format(coin.changePercent),
                changePercentColor = percentageFormatter.getTextColor(coin.changePercent),
                icon = resourceProvider.getIcon(coin.name),
            )
        }

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState = _uiState.asStateFlow()

    private companion object {
        val initialUiState = CoinListUiState(
            coinList = emptyList(),
            isLoading = true,
        )
    }
}
