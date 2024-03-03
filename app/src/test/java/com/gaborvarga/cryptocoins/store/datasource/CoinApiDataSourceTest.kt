package com.aldi.cryptocoins.store.datasource

import android.net.http.HttpException
import com.aldi.cryptocoins.architecture.dispatchers.DispatcherProvider
import com.aldi.cryptocoins.architecture.errortracker.ErrorTracker
import com.aldi.cryptocoins.model.AssetsResponseDTO
import com.aldi.cryptocoins.model.Coin
import com.aldi.cryptocoins.model.CoinDTO
import com.aldi.cryptocoins.testutils.TestDispatcherProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoinApiDataSourceTest {

    private val testCoinDTO =
        CoinDTO(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            priceUsd = "1.12",
            changePercent24Hr = "0.98",
            marketCapUsd = "122.0087",
            volumeUsd24Hr = "100.25",
            supply = "824.3"
        )
    private val testMalformedCoinDTO = testCoinDTO.copy(id = null)
    private val testCoin =
        Coin(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            priceUsd = "1.12",
            changePercent24Hr = "0.98",
            marketCapUsd = "122.0087",
            volumeUsd24Hr = "100.25",
            supply = "824.3"
        )
    private val testResponse =
        AssetsResponseDTO(
            data = listOf(testCoinDTO),
        )

    private lateinit var dispatcherProvider: DispatcherProvider
    private val errorTracker: ErrorTracker = mockk(relaxUnitFun = true)
    private val assetsApi: AssetsApi = mockk()

    private lateinit var dataSource: CoinApiDataSource

    private fun createDataSource() {
        dataSource = CoinApiDataSource(
            dispatcherProvider,
            errorTracker,
            assetsApi,
        )
    }

    private fun initTestDispatcherProvider(testScheduler: TestCoroutineScheduler) {
        dispatcherProvider = TestDispatcherProvider(testScheduler)
    }

    @Test
    fun `When the api call results in success, Then getCoinList() returns the mapped result`() = runTest {
        initTestDispatcherProvider(testScheduler)
        createDataSource()

        // When
        mockApiResponse(testResponse)
        val result = dataSource.getCoinList(1)
        val expected = listOf(testCoin)

        // Then
        assertEquals(
            expected,
            result,
        )
    }

    @Test
    fun `When the api call results in success but a required field is missing, Then getCoinList() handles the error and returns an empty list`() =
        runTest {
            initTestDispatcherProvider(testScheduler)
            createDataSource()

            // When
            val malformedResponse = testResponse.copy(
                data = listOf(testMalformedCoinDTO),
            )
            mockApiResponse(malformedResponse)
            val result = dataSource.getCoinList(1)
            val expected = emptyList<Coin>()

            // Then
            assertEquals(
                expected,
                result,
            )
        }

    @Test
    fun `When the api call results in success but a required field is missing, When getCoinList() is called, Then the error is tracked`() =
        runTest {
            initTestDispatcherProvider(testScheduler)
            createDataSource()

            // When
            val malformedResponse = testResponse.copy(
                data = listOf(testMalformedCoinDTO),
            )
            mockApiResponse(malformedResponse)
            dataSource.getCoinList(1)

            // Then
            coVerify { errorTracker.reportError(any(), any()) }
        }

    @Test
    fun `When the api call results in error, Then getCoinList() handles the error and returns an empty list`() =
        runTest {
            initTestDispatcherProvider(testScheduler)
            createDataSource()

            // When
            mockApiErrorResponse(
                HttpException("Internal server error", null)
            )
            val result = dataSource.getCoinList(1)
            val expected = emptyList<Coin>()

            // Then
            assertEquals(
                expected,
                result,
            )
        }

    @Test
    fun `Given the api call results in error, When getCoinList() is called, Then the error is tracked`() = runTest {
        initTestDispatcherProvider(testScheduler)
        createDataSource()

        // Given
        val httpException = HttpException("Internal server error", null)
        mockApiErrorResponse(httpException)

        // When
        dataSource.getCoinList(1)

        // Then
        coVerify { errorTracker.reportError(httpException, any()) }
    }

    private fun mockApiResponse(response: AssetsResponseDTO) {
        coEvery { assetsApi.getAssets(any()) } returns response
    }

    private fun mockApiErrorResponse(exception: Exception) {
        coEvery { assetsApi.getAssets(any()) } throws exception
    }
}
