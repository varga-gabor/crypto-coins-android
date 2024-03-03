package com.aldi.cryptocoins.store

import com.aldi.cryptocoins.model.Coin
import org.koin.core.annotation.Factory

@Factory
class CoinRepository(
    private val mockCoinDataSource: MockCoinDataSource,
) {

    suspend fun getCoinList(): List<Coin> = mockCoinDataSource.getMockCoinList()
}
