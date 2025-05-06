package tech.kotlinlang.ui

data class AmountTextConfig(
    val currency: AmountCurrency,
    val symbolRatio: Double,
    val decimalRatio: Double,
    val symbolFontWeightDiff: Int,
    val decimalFontWeightDiff: Int,
) {
    companion object {
        val Default: AmountTextConfig
            get() = AmountTextConfig(
                currency = AmountCurrency.IndianRupee,
                symbolRatio = 0.8,
                decimalRatio = 0.6,
                symbolFontWeightDiff = 1,
                decimalFontWeightDiff = 1,
            )
    }
}