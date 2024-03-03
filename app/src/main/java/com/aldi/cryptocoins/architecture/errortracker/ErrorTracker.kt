package com.aldi.cryptocoins.architecture.errortracker

interface ErrorTracker {

    fun reportError(throwable: Throwable, message: String? = null)
}
