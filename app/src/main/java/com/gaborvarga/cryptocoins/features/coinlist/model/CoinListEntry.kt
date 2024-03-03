package com.aldi.cryptocoins.features.coinlist.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class CoinListEntry(
    val name: String,
    val symbol: String,
    val price: String,
    val changePercent: String,
    @ColorRes val changePercentColor: Int,
    @DrawableRes val icon: Int,
)
