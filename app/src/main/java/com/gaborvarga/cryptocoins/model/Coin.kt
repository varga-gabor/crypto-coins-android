package com.aldi.cryptocoins.model

data class Coin(
    val name: String,
    val symbol: String,
    val price: String,
    val changePercent: String,
    val marketCap: String,
    val volume: String,
    val supply: String,
)
