package com.aldi.cryptocoins.features.coinlist.model

data class CoinListUiState(
    val coinList: List<CoinListEntry>,
    val isLoading: Boolean,
)
