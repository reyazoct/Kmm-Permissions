package tech.kotlinlang.ui.amountText

sealed class AmountCurrency {
    data object IndianRupee: AmountCurrency()

    val symbol: String
        get() = "₹"
}