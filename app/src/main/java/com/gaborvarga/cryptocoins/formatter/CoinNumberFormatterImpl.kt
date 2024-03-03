package com.aldi.cryptocoins.formatter

import com.aldi.cryptocoins.architecture.errortracker.ErrorTracker
import org.koin.core.annotation.Factory
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.IllegalArgumentException

@Factory
class CoinNumberFormatterImpl(
    private val errorTracker: ErrorTracker,
) : CoinNumberFormatter {

    private val units = listOf("", "K", "M", "B", "T", "Qa", "Qi")
    private val decimalFormat = DecimalFormat("#0.00")

    override fun abbreviate(numberString: String): String {
        val bigDecimal = try {
            BigDecimal(numberString)
        } catch (e: NumberFormatException) {
            errorTracker.reportError(e, "Could not abbreviate number: '$numberString'")
            return numberString
        }
        var integerPart = bigDecimal.setScale(0, RoundingMode.DOWN)
        val oneThousand = BigDecimal(1000)

        val noAbbreviationNeeded = (integerPart < oneThousand)
        if (noAbbreviationNeeded) {
            return tryFormat(bigDecimal) ?: numberString
        }

        var index = 0
        while (integerPart >= oneThousand) {
            integerPart = integerPart.movePointLeft(3)
            index += 1
        }

        integerPart = integerPart.setScale(2, RoundingMode.DOWN)
        val formattedIntegerPart = tryFormat(integerPart) ?: return numberString
        val unit = units[index]

        return "$formattedIntegerPart$unit"
    }

    private fun tryFormat(bigDecimal: BigDecimal): String? =
        try {
            decimalFormat.format(bigDecimal)
        } catch (e: IllegalArgumentException) {
            null
        }
}
