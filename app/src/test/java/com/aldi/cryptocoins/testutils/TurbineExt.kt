package com.aldi.cryptocoins.testutils

import app.cash.turbine.TurbineTestContext
import org.junit.jupiter.api.Assertions

suspend fun <T> TurbineTestContext<T>.assertNextItem(expected: T) {
    Assertions.assertEquals(expected, awaitItem())
}
