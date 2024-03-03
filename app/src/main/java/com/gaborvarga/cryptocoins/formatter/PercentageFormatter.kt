package com.aldi.cryptocoins.formatter

import androidx.annotation.ColorRes

interface PercentageFormatter {

    fun format(percentageString: String): String

    @ColorRes
    fun getTextColor(percentageString: String): Int
}
