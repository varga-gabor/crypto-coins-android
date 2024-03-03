package com.aldi.cryptocoins.store

import com.aldi.cryptocoins.model.Coin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import org.koin.core.annotation.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class AutoRefreshCoinsUseCase(
    private val autoRefreshParams: AutoRefreshParams,
    private val coinRepository: CoinRepository,
    externalScope: CoroutineScope,
) {

    private val intervalFlow = flow {
        while (currentCoroutineContext().isActive) {
            emit(Unit)
            delay(autoRefreshParams.refreshIntervalMs)
        }
    }

    val coinListFlow = intervalFlow
        .flatMapLatest {
            flow {
                emit(AutoRefreshResult.Pending)
                val coinList = coinRepository.getCoinList()
                emit(AutoRefreshResult.Success(coinList))
            }
        }
        .shareIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(autoRefreshParams.flowStopTimeoutMs),
            replay = 1,
        )

    sealed interface AutoRefreshResult {

        data object Pending : AutoRefreshResult
        data class Success(val coinList: List<Coin>) : AutoRefreshResult
    }
}
