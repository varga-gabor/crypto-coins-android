package com.aldi.cryptocoins.architecture.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Singleton

@Singleton
class DispatcherProviderImpl : DispatcherProvider {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate

    override val default: CoroutineDispatcher
        get() = Dispatchers.Default

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
