package tech.kotlinlang.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    val symbolFontWeightAndSize = remember {
        val symbolFontWeightInt = style.fontWeight?.weight?.minus(amountTextConfig.symbolFontWeightDiff * 100)
        val fontWeight = if (symbolFontWeightInt != null && symbolFontWeightInt < 100) {
            FontWeight.Light
        } else if (symbolFontWeightInt != null) {
            FontWeight(symbolFontWeightInt)
        } else {
            style.fontWeight
        }
        fontWeight to style.fontSize * amountTextConfig.symbolRatio
    }
    val decimalFontWeightAndSize = remember {
        val decimalFontWeightInt = style.fontWeight?.weight?.minus(amountTextConfig.decimalFontWeightDiff * 100)
        val fontWeight = if (decimalFontWeightInt != null && decimalFontWeightInt < 100) {
            FontWeight.Light
        } else if (decimalFontWeightInt != null) {
            FontWeight(decimalFontWeightInt)
        } else {
            style.fontWeight
        }
        fontWeight to style.fontSize * amountTextConfig.decimalRatio
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        AnimatedContent(
            targetState = amountTextConfig.currency.symbol,
            transitionSpec = {
                slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
            },
        ) { target ->
            Text(
                text = target,
                style = style.copy(
                    fontSize = symbolFontWeightAndSize.second,
                    fontWeight = symbolFontWeightAndSize.first,
                    lineHeight = style.fontSize,
                )
            )
        }
        integerPart.forEach { part ->
            AnimatedContent(
                targetState = part,
                transitionSpec = {
                    slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                },
            ) { target ->
                Text(
                    text = target.toString(),
                    style = style.copy(
                        lineHeight = style.fontSize,
                    )
                )
            }
        }
        decimalPart?.forEach { part ->
            AnimatedContent(
                targetState = part,
                transitionSpec = {
                    slideInVertically { height -> height } + fadeIn() togetherWith slideOutVertically { height -> -height } + fadeOut()
                },
            ) { target ->
                Text(
                    text = target.toString(),
                    style = style.copy(
                        fontSize = decimalFontWeightAndSize.second,
                        fontWeight = decimalFontWeightAndSize.first,
                        lineHeight = style.fontSize,
                    )
                )
            }
        }
    }
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
