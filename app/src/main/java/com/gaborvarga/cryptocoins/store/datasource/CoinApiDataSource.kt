package com.aldi.cryptocoins.store.datasource

import com.aldi.cryptocoins.architecture.dispatchers.DispatcherProvider
import com.aldi.cryptocoins.architecture.errortracker.ErrorTracker
import com.aldi.cryptocoins.model.Coin
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import retrofit2.Retrofit

@Factory
class CoinApiDataSource(
    retrofit: Retrofit,
    private val dispatcherProvider: DispatcherProvider,
    private val errorTracker: ErrorTracker,
) {

    private val api = retrofit.create(AssetsApi::class.java)

    suspend fun getCoinList(maxRank: Int): List<Coin> =
        withContext(dispatcherProvider.io) {
            val queryMap = mapOf(
                // The response coin list is sorted by Rank
                "limit" to maxRank.toString(),
            )
            try {
                api.getAssets(queryMap)
                    .toCoinList()
            } catch (t: Throwable) {
                errorTracker.reportError(t, "Error calling getCoinList()")
                emptyList()
            }
        }
}
