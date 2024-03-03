package com.aldi.cryptocoins.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

object KoinInitializer {

    fun init(applicationContext: Context) {
        startKoin {
            androidContext(applicationContext)
            modules(
                AppModule().module,
            )
        }
    }
}
