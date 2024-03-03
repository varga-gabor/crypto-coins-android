package com.aldi.cryptocoins.features.coindetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.architecture.navigation.api.Navigator
import com.aldi.cryptocoins.features.coindetails.model.CoinDetails
import com.aldi.cryptocoins.features.coindetails.model.CoinDetailsUiState
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase
import com.aldi.cryptocoins.store.AutoRefreshCoinsUseCase.AutoRefreshResult
import com.aldi.cryptocoins.store.model.Coin
import com.aldi.cryptocoins.testutils.assertNextItem
import com.aldi.cryptocoins.util.formatter.CoinNumberFormatter
import com.aldi.cryptocoins.util.formatter.PercentageFormatter
import com.aldi.cryptocoins.util.formatter.PriceFormatter
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoinDetailsViewModelTest {

    private val testCoinId = "ethereum"
    private val testPrice = "$1.0"
    private val testPercentage = "0.44%"
    private val testSupply = "127.08M"
    private val testColor = R.color.green

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

    private val emptyCoinDetails = CoinDetails(
        name = "",
        price = "",
        changePercent = "",
        changePercentColor = R.color.green,
        marketCap = "",
        volume = "",
        supply = "",
    )
    private val testCoinDetails = CoinDetails(
        name = "Ethereum",
        price = testPrice,
        changePercent = testPercentage,
        changePercentColor = testColor,
        marketCap = testPrice,
        volume = testPrice,
        supply = testSupply,
    )

    private val savedStateHandle: SavedStateHandle = mockk()
    private val autoRefreshCoinsUseCase: AutoRefreshCoinsUseCase = mockk()
    private val numberFormatter: CoinNumberFormatter = mockk {
        every { abbreviate(any()) } returns testSupply
    }
    private val priceFormatter: PriceFormatter = mockk {
        every { formatToUsdPrice(any()) } returns testPrice
    }
    private val percentageFormatter: PercentageFormatter = mockk {
        every { format(any()) } returns testPercentage
        every { getTextColor(any()) } returns testColor
    }
    private val navigator: Navigator = mockk(relaxUnitFun = true)

    private lateinit var viewModel: CoinDetailsViewModel

    private fun createViewModel() {
        viewModel = CoinDetailsViewModel(
            savedStateHandle,
            autoRefreshCoinsUseCase,
            priceFormatter,
            numberFormatter,
            percentageFormatter,
            navigator,
        )
    }

    @BeforeEach
    fun setup() {
        every { savedStateHandle.get<String>(any()) } returns testCoinId
    }

    @Test
    fun `When viewModel is created, Then initial UI state is emitted`() = runTest {
        // When
        createViewModel()

        // Then
        viewModel.uiState.test {
            val expectedState = CoinDetailsUiState(
                emptyCoinDetails,
                isLoading = false,
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
            skipItems(1) // Initial state

            // Then
            val expectedState = CoinDetailsUiState(
                emptyCoinDetails,
                isLoading = true,
            )
            assertNextItem(expectedState)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `Given onViewStarted is called, When autoRefreshState is Pending, Then the previous state is shown during loading`() =
        runTest {
            createViewModel()

            val coinListFlow = MutableSharedFlow<AutoRefreshResult>(replay = 1)
                .apply {
                    emit(AutoRefreshResult.Success(testCoinList))
                }
            coEvery { autoRefreshCoinsUseCase.coinListFlow } returns coinListFlow

            viewModel.uiState.test {
                skipItems(1) // Initial state

                // Given
                backgroundScope.launch {
                    viewModel.onViewStarted()
                }
                skipItems(1) // Success

                // When
                coinListFlow.emit(AutoRefreshResult.Pending)

                // Then
                val expectedState = CoinDetailsUiState(
                    testCoinDetails,
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
            val expectedState = CoinDetailsUiState(
                testCoinDetails,
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
