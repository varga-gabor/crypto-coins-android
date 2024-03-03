package com.aldi.cryptocoins.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinDTO(
    val id: String?,
    val name: String?,
    val symbol: String?,
    val priceUsd: String?,
    val changePercent24Hr: String?,
    val marketCapUsd: String?,
    val volumeUsd24Hr: String?,
    val supply: String?,
) {

    fun toModel(): Coin =
        Coin(
            id = id!!,
            name = name!!,
            symbol = symbol!!,
            priceUsd = priceUsd!!,
            changePercent24Hr = changePercent24Hr!!,
            marketCapUsd = marketCapUsd!!,
            volumeUsd24Hr = volumeUsd24Hr!!,
            supply = supply!!,
        )
}
