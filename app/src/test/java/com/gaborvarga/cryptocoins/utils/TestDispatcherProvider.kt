package com.aldi.cryptocoins.utils

import com.aldi.cryptocoins.architecture.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider(testScheduler: TestCoroutineScheduler) : DispatcherProvider {

    override val main: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val mainImmediate: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val default: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val io: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
    override val unconfined: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
}
