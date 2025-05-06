package tech.kotlinlang.permission.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.kotlinlang.ui.AmountText
import tech.kotlinlang.ui.AmountTextConfig
import tech.kotlinlang.ui.AmountTextConfigLocal

@Composable
fun UiComponentsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val commonModifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            )
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
        item {
            AmountTextContent(commonModifier)
        }
    }
}

@Composable
private fun AmountTextContent(modifier: Modifier) {
    CompositionLocalProvider(
        AmountTextConfigLocal provides AmountTextConfig.Default.copy(
            symbolRatio = 0.8,
            decimalRatio = 0.7,
            symbolFontWeightDiff = 3,
            decimalFontWeightDiff = 3,
        )
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            AmountText(
                amount = 220.13,
                style = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
            )
        }
    }
}