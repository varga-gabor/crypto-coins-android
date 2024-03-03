package com.aldi.cryptocoins.store

import com.aldi.cryptocoins.model.Coin
import com.aldi.cryptocoins.store.datasource.CoinApiDataSource
import org.koin.core.annotation.Factory

@Factory
class CoinRepository(
    private val coinApiDataSource: CoinApiDataSource,
) {

    suspend fun getCoinList(maxRank: Int): List<Coin> =
        coinApiDataSource.getCoinList(maxRank)
}
