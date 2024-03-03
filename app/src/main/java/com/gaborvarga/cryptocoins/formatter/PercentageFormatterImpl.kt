package com.aldi.cryptocoins.formatter

import androidx.annotation.ColorRes
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.architecture.errortracker.ErrorTracker
import org.koin.core.annotation.Factory
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

@Factory
class PercentageFormatterImpl(
    private val errorTracker: ErrorTracker,
) : PercentageFormatter {

    override fun format(percentageString: String): String {
        val percentage = try {
            BigDecimal(percentageString).setScale(2, RoundingMode.DOWN)
        } catch (e: NumberFormatException) {
            errorTracker.reportError(e, "Could not format percentage: '$percentageString'")
            return percentageString
        }

        val formatted = DecimalFormat("#0.00").format(percentage)
        return "$formatted%"
    }

    @ColorRes
    override fun getTextColor(percentageString: String): Int {
        val percentage = try {
            BigDecimal(percentageString).setScale(2, RoundingMode.DOWN)
        } catch (e: NumberFormatException) {
            errorTracker.reportError(e, "Could not get percentage text color for: '$percentageString'")
            return R.color.dark_gray
        }

        return if (percentage >= BigDecimal(0)) {
            R.color.green
        } else {
            R.color.red
        }
    }
}
