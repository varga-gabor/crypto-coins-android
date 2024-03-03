package com.aldi.cryptocoins.formatter

import com.aldi.cryptocoins.architecture.errortracker.ErrorTracker
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CoinNumberFormatterTest {

    private val errorTracker: ErrorTracker = mockk(relaxUnitFun = true)

    private lateinit var coinNumberFormatter: CoinNumberFormatterImpl

    private fun createCoinNumberFormatter() {
        coinNumberFormatter = CoinNumberFormatterImpl(
            errorTracker,
        )
    }

    @Test
    fun `Given invalid input, When abbreviate() is called, Then the unaltered input is returned`() {
        // Given
        createCoinNumberFormatter()
        val inputNumber = "really_invalid"

        // When
        val result = coinNumberFormatter.abbreviate(inputNumber)

        // Then
        assertEquals(
            inputNumber,
            result,
        )
    }

    @Test
    fun `Given invalid input, When abbreviate() is called, Then error is tracked`() {
        // Given
        createCoinNumberFormatter()
        val inputNumber = "really_invalid"

        // When
        coinNumberFormatter.abbreviate(inputNumber)

        // Then
        verify {
            errorTracker.reportError(any(), any())
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun abbreviateParameterisedTest(testCase: TestCase) {
        // Given
        val (inputNumber, expectedOutput) = testCase
        createCoinNumberFormatter()

        // Then
        assertEquals(
            expectedOutput,
            coinNumberFormatter.abbreviate(inputNumber),
        )
    }

    data class TestCase(
        val input: String,
        val expectedOutput: String,
    )

    companion object {

        @JvmStatic
        fun parameters(): Stream<Arguments> = Stream.of(
            Arguments.of(
                Named.of(
                    "Integer part of input number is zero",
                    TestCase(
                        input = "0.0123",
                        expectedOutput = "0.01",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely above zero",
                    TestCase(
                        input = "1.0123",
                        expectedOutput = "1.01",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is below 1000",
                    TestCase(
                        input = "12.0123",
                        expectedOutput = "12.01",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely below 1000",
                    TestCase(
                        input = "999.0123",
                        expectedOutput = "999.01",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is 1000",
                    TestCase(
                        input = "1000.0123",
                        expectedOutput = "1.00K",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely above 1000",
                    TestCase(
                        input = "1001.0123",
                        expectedOutput = "1.00K",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is between 1000 and 1 Million",
                    TestCase(
                        input = "199800.0123",
                        expectedOutput = "199.80K",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely below 1 Million",
                    TestCase(
                        input = "999999.0123",
                        expectedOutput = "999.99K",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is 1 Million",
                    TestCase(
                        input = "1000000.0123",
                        expectedOutput = "1.00M",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely above 1 Million",
                    TestCase(
                        input = "1001000.0123",
                        expectedOutput = "1.00M",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is between 1 Million and 1 Billion",
                    TestCase(
                        input = "356200000.0123",
                        expectedOutput = "356.20M",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely below 1 Billion",
                    TestCase(
                        input = "999999999.0123",
                        expectedOutput = "999.99M",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is 1 Billion",
                    TestCase(
                        input = "1000000000.0123",
                        expectedOutput = "1.00B",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely above 1 Billion",
                    TestCase(
                        input = "1001000000.0123",
                        expectedOutput = "1.00B",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is between 1 Billion and 1 Trillion",
                    TestCase(
                        input = "251900000000.0123",
                        expectedOutput = "251.90B",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely below 1 Trillion",
                    TestCase(
                        input = "999999999999.0123",
                        expectedOutput = "999.99B",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is 1 Trillion",
                    TestCase(
                        input = "1000000000000.0123",
                        expectedOutput = "1.00T",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely above 1 Trillion",
                    TestCase(
                        input = "1001000000000.0123",
                        expectedOutput = "1.00T",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is above 1 Trillion",
                    TestCase(
                        input = "12700000000000.0123",
                        expectedOutput = "12.70T",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely below 1 Quadrillion",
                    TestCase(
                        input = "999999999999999.0123",
                        expectedOutput = "999.99T",
                    ),
                ),
            ),
        )
    }
}
