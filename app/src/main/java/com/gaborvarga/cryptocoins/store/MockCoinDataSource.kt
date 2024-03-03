package com.aldi.cryptocoins.store

import com.aldi.cryptocoins.model.Coin
import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory

@Factory
class MockCoinDataSource {

    private val mockCoinList = listOf(
        Coin(
            name = "Bitcoin",
            symbol = "BTC",
            price = "62841.3776139956093479",
            changePercent = "9.6523694325187566",
            marketCap = "1233703496352.4021526082064975",
            volume = "51966478443.6365156428928833",
            supply = "19632025.0000000000000000",
        ),
        Coin(
            name = "Ethereum",
            symbol = "ETH",
            price = "345.0042203554732305",
            changePercent = "-1.9244179612763754",
            marketCap = "415532848998.7375529962780354",
            volume = "25664381955.6628598844923086",
            supply = "120165512.3937419400000000",
        ),
    )

    suspend fun getMockCoinList(): List<Coin> {
        delay(2000L)
        return mockCoinList
    }
}
