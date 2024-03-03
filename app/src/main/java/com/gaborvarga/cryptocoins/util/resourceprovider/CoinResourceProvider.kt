package com.aldi.cryptocoins.util.resourceprovider

import androidx.annotation.DrawableRes

interface CoinResourceProvider {

    @DrawableRes
    fun getIcon(coinName: String): Int
}
