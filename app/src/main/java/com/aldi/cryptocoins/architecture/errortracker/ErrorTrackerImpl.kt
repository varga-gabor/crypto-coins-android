package com.aldi.cryptocoins.architecture.errortracker

import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class ErrorTrackerImpl : ErrorTracker {

    override fun reportError(throwable: Throwable, message: String?) {
        // Just for debug use.
        // In release builds, we could send these errors into Crashlytics (or alternatives).
        Timber.e(throwable, message)
    }
}
