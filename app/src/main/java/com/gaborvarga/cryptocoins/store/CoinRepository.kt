package com.aldi.cryptocoins.store

import com.aldi.cryptocoins.store.datasource.CoinApiDataSource
import com.aldi.cryptocoins.store.model.Coin
import org.koin.core.annotation.Factory

@Factory
class CoinRepository(
    private val coinApiDataSource: CoinApiDataSource,
) {

    suspend fun getCoinList(maxRank: Int): List<Coin> =
        coinApiDataSource.getCoinList(maxRank)
}
