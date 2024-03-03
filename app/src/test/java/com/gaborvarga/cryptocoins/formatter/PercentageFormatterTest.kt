package com.aldi.cryptocoins.formatter

import com.aldi.cryptocoins.R
import com.aldi.cryptocoins.architecture.errortracker.ErrorTracker
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class PercentageFormatterTest {

    private val errorTracker: ErrorTracker = mockk(relaxUnitFun = true)

    private lateinit var percentageFormatter: PercentageFormatterImpl

    private fun createPercentageFormatter() {
        percentageFormatter = PercentageFormatterImpl(
            errorTracker,
        )
    }

    @Test
    fun `Given invalid input, When format() is called, Then the unaltered input is returned`() {
        // Given
        createPercentageFormatter()
        val inputNumber = "really_invalid"

        // When
        val result = percentageFormatter.format(inputNumber)

        // Then
        Assertions.assertEquals(
            inputNumber,
            result,
        )
    }

    @Test
    fun `Given invalid input, When format() is called, Then error is tracked`() {
        // Given
        createPercentageFormatter()
        val inputNumber = "really_invalid"

        // When
        percentageFormatter.format(inputNumber)

        // Then
        verify {
            errorTracker.reportError(any(), any())
        }
    }

    @Test
    fun `Given invalid input, When getTextColor() is called, Then dark grey color is returned`() {
        // Given
        createPercentageFormatter()
        val inputNumber = "really_invalid"

        // When
        val expected = R.color.dark_gray
        val result = percentageFormatter.getTextColor(inputNumber)

        // Then
        Assertions.assertEquals(
            expected,
            result,
        )
    }

    @Test
    fun `Given invalid input, When getTextColor() is called, Then error is tracked`() {
        // Given
        createPercentageFormatter()
        val inputNumber = "really_invalid"

        // When
        percentageFormatter.getTextColor(inputNumber)

        // Then
        verify {
            errorTracker.reportError(any(), any())
        }
    }

    @Test
    fun `Given input is zero, When getTextColor() is called, Then green color is returned`() {
        // Given
        createPercentageFormatter()
        val inputNumber = "0.0"

        // When
        val expected = R.color.green
        val result = percentageFormatter.getTextColor(inputNumber)

        // Then
        Assertions.assertEquals(
            expected,
            result,
        )
    }

    @Test
    fun `Given input is a positive number, When getTextColor() is called, Then green color is returned`() {
        // Given
        createPercentageFormatter()
        val inputNumber = "27.1"

        // When
        val expected = R.color.green
        val result = percentageFormatter.getTextColor(inputNumber)

        // Then
        Assertions.assertEquals(
            expected,
            result,
        )
    }

    @Test
    fun `Given input is a negative number, When getTextColor() is called, Then red color is returned`() {
        // Given
        createPercentageFormatter()
        val inputNumber = "-1.0"

        // When
        val expected = R.color.red
        val result = percentageFormatter.getTextColor(inputNumber)

        // Then
        Assertions.assertEquals(
            expected,
            result,
        )
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun formatParameterisedTest(testCase: TestCase) {
        // Given
        val (inputNumber, expectedOutput) = testCase
        createPercentageFormatter()

        // Then
        Assertions.assertEquals(
            expectedOutput,
            percentageFormatter.format(inputNumber),
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
                    "Input is zero",
                    TestCase(
                        input = "0.0",
                        expectedOutput = "0.00%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Integer part of input number is zero",
                    TestCase(
                        input = "0.0123",
                        expectedOutput = "0.01%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is negative",
                    TestCase(
                        input = "-0.0123",
                        expectedOutput = "-0.01%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely above zero",
                    TestCase(
                        input = "1.0123",
                        expectedOutput = "1.01%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is below 100",
                    TestCase(
                        input = "52.123",
                        expectedOutput = "52.12%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely below 100",
                    TestCase(
                        input = "99.999",
                        expectedOutput = "99.99%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is 100",
                    TestCase(
                        input = "100.0",
                        expectedOutput = "100.00%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely above 100",
                    TestCase(
                        input = "100.01",
                        expectedOutput = "100.01%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is above 100",
                    TestCase(
                        input = "128.5",
                        expectedOutput = "128.50%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is barely below 1000",
                    TestCase(
                        input = "999.9999",
                        expectedOutput = "999.99%",
                    ),
                ),
            ),
            Arguments.of(
                Named.of(
                    "Input is 1000",
                    TestCase(
                        input = "1000.0",
                        expectedOutput = "1000.00%",
                    ),
                ),
            ),
        )
    }
}
