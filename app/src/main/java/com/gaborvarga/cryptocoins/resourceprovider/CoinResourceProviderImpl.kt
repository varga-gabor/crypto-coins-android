package com.aldi.cryptocoins.resourceprovider

import androidx.annotation.DrawableRes
import com.aldi.cryptocoins.R
import org.koin.core.annotation.Factory
import java.util.Locale

@Factory
class CoinResourceProviderImpl : CoinResourceProvider {

    private val iconMap = mapOf(
        "bitcoin" to R.drawable.ic_bitcoin,
        "ethereum" to R.drawable.ic_ethereum,
        "xrp" to R.drawable.ic_xrp,
        "tether" to R.drawable.ic_tether,
        "bnb" to R.drawable.ic_bnb,
        "cardano" to R.drawable.ic_cardano,
        "avalanche" to R.drawable.ic_avalanche,
        "polygon" to R.drawable.ic_polygon,
    )

    @DrawableRes
    override fun getIcon(coinName: String): Int =
        iconMap.getOrDefault(
            key = coinName.lowercase(Locale.ROOT),
            defaultValue = R.drawable.ic_bitcoin,
        )
}
