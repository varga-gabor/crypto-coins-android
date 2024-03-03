package com.aldi.cryptocoins.store

import org.koin.core.annotation.Factory

@Factory
data class AutoRefreshParams(
    val refreshIntervalMs: Long = 1000 * 60, // 1 minute
    val flowStopTimeoutMs: Long = 5000,
)
