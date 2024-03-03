package com.aldi.cryptocoins.features.coindetails.model

import androidx.annotation.ColorRes

data class CoinDetails(
    val name: String,
    val price: String,
    val changePercent: String,
    @ColorRes val changePercentColor: Int,
    val marketCap: String,
    val volume: String,
    val supply: String,
)
