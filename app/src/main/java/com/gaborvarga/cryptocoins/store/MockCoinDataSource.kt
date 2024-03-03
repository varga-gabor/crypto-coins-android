package com.aldi.cryptocoins.store

import com.aldi.cryptocoins.model.Coin
import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory

@Factory
class MockCoinDataSource {

    private val mockCoinList = listOf(
        Coin(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            priceUsd = "62841.3776139956093479",
            changePercent24Hr = "9.6523694325187566",
            marketCapUsd = "1233703496352.4021526082064975",
            volumeUsd24Hr = "51966478443.6365156428928833",
            supply = "19632025.0000000000000000",
        ),
        Coin(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            priceUsd = "345.0042203554732305",
            changePercent24Hr = "-1.9244179612763754",
            marketCapUsd = "415532848998.7375529962780354",
            volumeUsd24Hr = "25664381955.6628598844923086",
            supply = "120165512.3937419400000000",
        ),
    )

    suspend fun getMockCoinList(): List<Coin> {
        delay(2000L)
        return mockCoinList
    }
}
