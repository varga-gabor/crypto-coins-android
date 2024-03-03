package com.aldi.cryptocoins.features.coinlist

import app.cash.turbine.test
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.features.coinlist.model.CoinListEntry
import com.aldi.cryptocoins.features.coinlist.model.CoinListUiState
import com.aldi.cryptocoins.formatter.PercentageFormatter
import com.aldi.cryptocoins.formatter.PriceFormatter
import com.aldi.cryptocoins.model.Coin
import com.aldi.cryptocoins.resourceprovider.CoinResourceProvider
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase.AutoRefreshResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoinListViewModelTest {

    private val testCoinList = listOf(
        Coin(
            name = "Ethereum",
            symbol = "ETH",
            price = "1.0",
            changePercent = "0.99",
            marketCap = "1.0",
            volume = "1.0",
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

    private lateinit var viewModel: CoinListViewModel

    private fun createViewModel() {
        viewModel = CoinListViewModel(
            autoRefreshCoinsUseCase,
            priceFormatter,
            percentageFormatter,
            resourceProvider,
        )
    }

    @Test
    fun `When viewModel is created, Then initial UI state is emitted`() = runTest {
        // When
        createViewModel()

        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            val expectedState = CoinListUiState(
                emptyList(),
                isLoading = true,
            )

            assertEquals(
                expectedState,
                initialState,
            )
        }
    }

    @Test
    fun `Given onViewStarted is called, When autoRefreshState is Pending, Then loading is shown`() = runTest {
        // When
        createViewModel()

        val coinListFlow = MutableSharedFlow<AutoRefreshResult>(replay = 1)
            .apply {
                emit(AutoRefreshResult.Pending)
            }
        coEvery { autoRefreshCoinsUseCase.coinListFlow } returns coinListFlow

        // Then
        viewModel.uiState.test {
            val job = launch {
                viewModel.onViewStarted()
            }
            val uiState = awaitItem()
            val expectedState = CoinListUiState(
                emptyList(),
                isLoading = true,
            )

            assertEquals(
                expectedState,
                uiState,
            )
            job.cancel()
        }
    }

    @Test
    fun `Given onViewStarted is called, When autoRefreshState is Success, Then UI state is updated`() = runTest {
        // When
        createViewModel()

        val coinListFlow = MutableSharedFlow<AutoRefreshResult>(replay = 1)
            .apply {
                emit(AutoRefreshResult.Success(testCoinList))
            }
        coEvery { autoRefreshCoinsUseCase.coinListFlow } returns coinListFlow

        // Then
        viewModel.uiState.test {
            val job = launch {
                viewModel.onViewStarted()
            }
            val initialState = awaitItem()
            val uiState = awaitItem()
            val expectedState = CoinListUiState(
                listOf(
                    CoinListEntry(
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

            assertEquals(
                expectedState,
                uiState,
            )
            job.cancel()
        }
    }
}
