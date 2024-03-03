package com.aldi.cryptocoins.features.coinlist

import app.cash.turbine.test
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.architecture.navigation.api.Navigator
import com.aldi.cryptocoins.features.coinlist.model.CoinListEntry
import com.aldi.cryptocoins.features.coinlist.model.CoinListUiState
import com.aldi.cryptocoins.model.Coin
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase.AutoRefreshResult
import com.aldi.cryptocoins.testutils.assertNextItem
import com.aldi.cryptocoins.util.formatter.PercentageFormatter
import com.aldi.cryptocoins.util.formatter.PriceFormatter
import com.aldi.cryptocoins.util.resourceprovider.CoinResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CoinListViewModelTest {

    private val testCoinList = listOf(
        Coin(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            priceUsd = "1.0",
            changePercent24Hr = "0.99",
            marketCapUsd = "1.0",
            volumeUsd24Hr = "1.0",
            supply = "1.0",
        )
    )

    private val autoRefreshCoinsUseCase: AutoRefreshCoinsUseCase = mockk()
    private val priceFormatter: PriceFormatter = mockk {
        every { formatToUsdPrice(any()) } returns "$1.0"
    }
    private val percentageFormatter: PercentageFormatter = mockk {
        every { format(any()) } returns "0.99%"
        every { getTextColor(any()) } returns R.color.green
    }
    private val resourceProvider: CoinResourceProvider = mockk {
        every { getIcon(any()) } returns R.drawable.ic_ethereum
    }
    private val navigator: Navigator = mockk(relaxUnitFun = true)

    private lateinit var viewModel: CoinListViewModel

    private fun createViewModel() {
        viewModel = CoinListViewModel(
            autoRefreshCoinsUseCase,
            priceFormatter,
            percentageFormatter,
            resourceProvider,
            navigator,
        )
    }

    @Test
    fun `When viewModel is created, Then initial UI state is emitted`() = runTest {
        // When
        createViewModel()

        // Then
        viewModel.uiState.test {
            val expectedState = CoinListUiState(
                emptyList(),
                isLoading = true,
            )

            assertNextItem(expectedState)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `Given onViewStarted is called, When autoRefreshState is Pending, Then loading is shown`() = runTest {
        createViewModel()

        viewModel.uiState.test {
            // Given
            backgroundScope.launch {
                viewModel.onViewStarted()
            }

            // When
            mockCoinListFlow(AutoRefreshResult.Pending)

            // Then
            val expectedState = CoinListUiState(
                emptyList(),
                isLoading = true,
            )

            assertNextItem(expectedState)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `Given onViewStarted is called, When autoRefreshState is Success, Then UI state is updated`() = runTest {
        createViewModel()

        viewModel.uiState.test {
            skipItems(1) // Initial state

            // Given
            backgroundScope.launch {
                viewModel.onViewStarted()
            }

            // When
            mockCoinListFlow(AutoRefreshResult.Success(testCoinList))

            // Then
            val expectedState = CoinListUiState(
                listOf(
                    CoinListEntry(
                        id = "ethereum",
                        name = "Ethereum",
                        symbol = "ETH",
                        price = "$1.0",
                        changePercent = "0.99%",
                        changePercentColor = R.color.green,
                        icon = R.drawable.ic_ethereum,
                    )
                ),
                isLoading = false,
            )
            assertNextItem(expectedState)
            ensureAllEventsConsumed()
        }
    }

    private suspend fun mockCoinListFlow(autoRefreshResult: AutoRefreshResult) {
        val coinListFlow = MutableSharedFlow<AutoRefreshResult>(replay = 1)
            .apply {
                emit(autoRefreshResult)
            }
        coEvery { autoRefreshCoinsUseCase.coinListFlow } returns coinListFlow
    }
}
