package com.aldi.cryptocoins.formatter

interface PriceFormatter {

    fun formatToUsdPrice(numberString: String): String
}
