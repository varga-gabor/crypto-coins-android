package com.aldi.cryptocoins.config

import timber.log.Timber
import timber.log.Timber.DebugTree

object DebugToolsInitializer {

    fun init() {
        Timber.plant(DebugTree())
    }
}
