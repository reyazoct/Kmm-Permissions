package tech.kotlinlang.ui

sealed class AmountCurrency {
    data object IndianRupee: AmountCurrency()

    val symbol: String
        get() = "₹"
}