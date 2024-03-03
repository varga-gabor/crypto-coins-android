package com.aldi.cryptocoins.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssetsResponseDTO(
    val data: List<CoinDTO>,
) {

    fun toCoinList(): List<Coin> =
        data.map { coinDTO ->
            coinDTO.toModel()
        }
}
