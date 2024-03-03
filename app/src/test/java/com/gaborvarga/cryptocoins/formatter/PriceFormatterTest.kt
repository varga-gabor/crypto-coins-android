package com.aldi.cryptocoins.formatter

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PriceFormatterTest {

    private val coinNumberFormatter: CoinNumberFormatter = mockk()

    private lateinit var priceFormatter: PriceFormatterImpl

    private fun createPriceFormatter() {
        priceFormatter = PriceFormatterImpl(
            coinNumberFormatter,
        )
    }

    @Test
    fun `When formatToUsdPrice() is called, Then the dollar sign is prepended`() {
        createPriceFormatter()
        val inputNumber = "1.0"

        every { coinNumberFormatter.abbreviate(any()) } returns "1.0"

        // When
        val expected = "$1.0"
        val result = priceFormatter.formatToUsdPrice(inputNumber)

        // Then
        Assertions.assertEquals(
            expected,
            result,
        )
    }
}
