package com.aldi.cryptocoins.util.formatter

import androidx.annotation.ColorRes

interface PercentageFormatter {

    fun format(percentageString: String): String

    @ColorRes
    fun getTextColor(percentageString: String): Int
}
