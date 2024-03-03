package com.aldi.cryptocoins.util.formatter

interface PriceFormatter {

    fun formatToUsdPrice(numberString: String): String
}
