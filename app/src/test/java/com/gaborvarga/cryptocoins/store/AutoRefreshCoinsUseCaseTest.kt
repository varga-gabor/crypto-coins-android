package com.aldi.cryptocoins.store

import app.cash.turbine.test
import com.aldi.cryptocoins.model.Coin
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase.AutoRefreshResult
import com.aldi.cryptocoins.utils.assertNextItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AutoRefreshCoinsUseCaseTest {

    private val testCoinList = listOf(
        Coin(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            priceUsd = "1.12",
            changePercent24Hr = "0.98",
            marketCapUsd = "122.0087",
            volumeUsd24Hr = "100.25",
            supply = "824.3"
        ),
    )
    private val autoRefreshIntervalMs: Long = 1000 * 60 // 1 minute

    private val autoRefreshParams: AutoRefreshParams = mockk {
        every { refreshIntervalMs } returns autoRefreshIntervalMs
        every { flowStopTimeoutMs } returns 0
        every { pendingDelayMs } returns 0
    }
    private val coinRepository: CoinRepository = mockk {
        coEvery { getCoinList(any()) } returns testCoinList
    }
    private lateinit var useCase: AutoRefreshCoinsUseCase

    private fun createUseCase(testScope: TestScope) {
        useCase = AutoRefreshCoinsUseCase(
            autoRefreshParams,
            coinRepository,
            externalScope = testScope,
        )
    }

    @Test
    fun `When coinListFlow is collected, Then Pending is emitted initially, followed by Success`() {
        val externalScope = TestScope(UnconfinedTestDispatcher())
        createUseCase(externalScope)

        runTest {
            // When
            useCase.coinListFlow.test {
                // Then
                assertNextItem(AutoRefreshResult.Pending)
                assertNextItem(AutoRefreshResult.Success(testCoinList))
                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given coinListFlow is collected, When refresh interval is elapsed, Then Pending is emitted again, followed by Success`() {
        val externalScope = TestScope(UnconfinedTestDispatcher())
        createUseCase(externalScope)

        runTest {
            // Given
            useCase.coinListFlow.test {
                skipItems(2) // Pending, Success

                // When
                externalScope.testScheduler.advanceTimeBy(autoRefreshIntervalMs + 1)

                // Then
                assertNextItem(AutoRefreshResult.Pending)
                assertNextItem(AutoRefreshResult.Success(testCoinList))
                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `When coinListFlow has multiple collectors, Then the latest item is emitted for new collectors`() {
        val externalScope = TestScope(UnconfinedTestDispatcher())
        createUseCase(externalScope)

        runTest {
            useCase.coinListFlow.test {
                assertNextItem(AutoRefreshResult.Pending)
                assertNextItem(AutoRefreshResult.Success(testCoinList))

                // When
                useCase.coinListFlow.test {
                    // Then
                    assertNextItem(AutoRefreshResult.Success(testCoinList))
                    ensureAllEventsConsumed()
                }
                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given coinListFlow has multiple collectors, When refresh interval is elapsed, Then Pending, followed by Success are emitted for every collector`() {
        val externalScope = TestScope(UnconfinedTestDispatcher())
        createUseCase(externalScope)

        runTest {
            // Given
            useCase.coinListFlow.test {
                skipItems(2) // Pending, Success

                useCase.coinListFlow.test {
                    skipItems(1) // Success (latest item)

                    // When
                    externalScope.testScheduler.advanceTimeBy(autoRefreshIntervalMs + 1)

                    // Then
                    assertNextItem(AutoRefreshResult.Pending)
                    assertNextItem(AutoRefreshResult.Success(testCoinList))
                    ensureAllEventsConsumed()
                }

                // Then
                assertNextItem(AutoRefreshResult.Pending)
                assertNextItem(AutoRefreshResult.Success(testCoinList))
                ensureAllEventsConsumed()
            }
        }
    }
}
