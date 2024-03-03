package com.aldi.cryptocoins.features.coindetails.navigation

import com.aldi.cryptocoins.architecture.navigation.api.Destination

data class CoinDetailsDestination(val coinId: String) : Destination {

    companion object {
        const val EXTRA_COIN_ID = "coinId"
    }
}
