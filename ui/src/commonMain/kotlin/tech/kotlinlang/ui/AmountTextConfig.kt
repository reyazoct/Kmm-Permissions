package tech.kotlinlang.ui

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class AmountTextConfig(
    val currency: AmountCurrency,
    val symbolRatio: Double,
    val decimalRatio: Double,
    val symbolFontWeightDiff: Int,
    val decimalFontWeightDiff: Int,
    val transitionDuration: Duration,
) {
    companion object {
        val Default: AmountTextConfig
            get() = AmountTextConfig(
                currency = AmountCurrency.IndianRupee,
                symbolRatio = 0.8,
                decimalRatio = 0.6,
                symbolFontWeightDiff = 1,
                decimalFontWeightDiff = 1,
                transitionDuration = 300.milliseconds,
            )
    }
}