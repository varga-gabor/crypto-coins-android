package com.aldi.cryptocoins.resourceprovider

import com.aldi.cryptocoins.R
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoinResourceProviderTest {

    private lateinit var coinResourceProvider: CoinResourceProviderImpl

    private fun createCoinResourceProvider() {
        coinResourceProvider = CoinResourceProviderImpl()
    }

    @Test
    fun `When the coin name is known, Then getIcon() returns the correct icon resource`() {
        // When
        createCoinResourceProvider()
        val coinName = "Ethereum"

        // Then
        assertEquals(
            R.drawable.ic_ethereum,
            coinResourceProvider.getIcon(coinName),
        )
    }

    @Test
    fun `When the coin name is unknown, Then getIcon() returns the fallback icon resource`() {
        // When
        createCoinResourceProvider()
        val coinName = "never_heard_about_this"

        // Then
        assertEquals(
            R.drawable.ic_bitcoin,
            coinResourceProvider.getIcon(coinName),
        )
    }
}
