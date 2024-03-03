package com.aldi.cryptocoins.store.model

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val supply: String,
)
