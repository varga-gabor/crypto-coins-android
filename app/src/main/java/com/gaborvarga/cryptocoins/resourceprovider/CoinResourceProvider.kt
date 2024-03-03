package com.aldi.cryptocoins.resourceprovider

import androidx.annotation.DrawableRes

interface CoinResourceProvider {

    @DrawableRes
    fun getIcon(coinName: String): Int
}
