package com.aldi.cryptocoins.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan("com.aldi.cryptocoins")
class AppModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Singleton
    fun externalScope(): CoroutineScope = GlobalScope
}
