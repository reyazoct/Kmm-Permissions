package tech.kotlinlang.ui

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import kotlin.math.roundToInt

@Composable
fun AmountText(
    modifier: Modifier = Modifier,
    amount: Double,
    amountTextConfig: AmountTextConfig = AmountTextConfigLocal.current,
    style: TextStyle = LocalTextStyle.current,
) {
    val integerPart = remember(amount) { formatStringWithCommas(amount.toInt().toString()) }
    val decimalPart = remember(amount) {
        val roundedAmount = (amount * 100).roundToInt() / 100.0
        val dec = ((roundedAmount - amount.toInt()) * 100).toInt()
        if (dec > 0) ".${dec.toString().padStart(2, '0')}" else null
    }
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            val symbolFontWeightInt = style.fontWeight?.weight?.minus(amountTextConfig.symbolFontWeightDiff * 100)
            val symbolFontWeight = if (symbolFontWeightInt != null && symbolFontWeightInt < 100) {
                FontWeight.Light
            } else if (symbolFontWeightInt != null) {
                FontWeight(symbolFontWeightInt)
            } else {
                style.fontWeight
            }
            withStyle(
                style.toSpanStyle().copy(
                    fontSize = style.fontSize * amountTextConfig.symbolRatio,
                    fontWeight = symbolFontWeight,
                )
            ) {
                append(amountTextConfig.currency.symbol)
            }
            append(integerPart)
            if (decimalPart != null) {
                val decimalFontWeightInt = style.fontWeight?.weight?.minus(amountTextConfig.decimalFontWeightDiff * 100)
                val decimalFontWeight = if (decimalFontWeightInt != null && decimalFontWeightInt < 100) {
                    FontWeight.Light
                } else if (decimalFontWeightInt != null) {
                    FontWeight(decimalFontWeightInt)
                } else {
                    style.fontWeight
                }
                withStyle(
                    style.toSpanStyle().copy(
                        fontSize = style.fontSize * amountTextConfig.decimalRatio,
                        fontWeight = decimalFontWeight,
                    ),
                ) {
                    append(decimalPart)
                }
            }
        },
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

private fun formatStringWithCommas(input: String): String {
    val reversedInput = input.reversed()
    val result = StringBuilder()
    var count = 0
    var firstRound = true

    for (char in reversedInput) {
        if (firstRound && count == 3) {
            result.append(',')
            count = 0
            firstRound = false
        } else if (!firstRound && count == 2) {
            result.append(',')
            count = 0
        }

        result.append(char)
        count++
    }

    return result.reverse().toString()
}

val AmountTextConfigLocal = staticCompositionLocalOf { AmountTextConfig.Default }
