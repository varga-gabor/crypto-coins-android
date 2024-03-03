package com.aldi.cryptocoins.store.datasource.dto

import com.aldi.cryptocoins.store.model.Coin
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
