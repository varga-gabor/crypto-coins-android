package com.aldi.cryptocoins

import android.app.Application
import com.aldi.cryptocoins.config.DebugToolsInitializer
import com.aldi.cryptocoins.di.KoinInitializer

class CryptoCoinsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugToolsInitializer.init()
        KoinInitializer.init(this)
    }
}
