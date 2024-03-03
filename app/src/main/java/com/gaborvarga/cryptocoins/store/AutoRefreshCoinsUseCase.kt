package com.aldi.cryptocoins.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import org.koin.core.annotation.Singleton

@Singleton
class AutoRefreshCoinsUseCase(
    private val autoRefreshParams: AutoRefreshParams,
    private val coinRepository: CoinRepository,
    externalScope: CoroutineScope,
) {

    private val forceRefreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    private val intervalFlow = flow {
        while (currentCoroutineContext().isActive) {
            delay(autoRefreshParams.refreshIntervalMs)
            emit(Unit)
        }
    }

    val coinListFlow = merge(
        forceRefreshTrigger,
        intervalFlow,
    )
        .map { coinRepository.getCoinList() }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(autoRefreshParams.flowStopTimeoutMs),
            initialValue = emptyList(),
        )
        .onSubscription { forceRefreshTrigger.emit(Unit) }
}
