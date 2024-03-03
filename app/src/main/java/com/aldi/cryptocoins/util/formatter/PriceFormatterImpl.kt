package com.aldi.cryptocoins.util.formatter

import org.koin.core.annotation.Factory

@Factory
class PriceFormatterImpl(
    private val coinNumberFormatter: CoinNumberFormatter,
) : PriceFormatter {

    override fun formatToUsdPrice(numberString: String): String {
        val abbreviated = coinNumberFormatter.abbreviate(numberString)
        return "$$abbreviated"
    }
}
