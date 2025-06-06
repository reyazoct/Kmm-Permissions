package tech.kotlinlang.permission.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quantityChanger.QuantityChanger
import quantityChanger.QuantityChangerConfig
import tech.kotlinlang.ui.amountText.AmountText
import tech.kotlinlang.ui.amountText.AmountTextConfig
import tech.kotlinlang.ui.amountText.AmountTextConfigLocal
import kotlin.random.Random
import tech.kotlinlang.image.KImage
import tech.kotlinlang.image.KImageType
import tech.kotlinlang.image.rememberKImageState

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
        item {
            QuantityChangerContent(commonModifier)
        }
        item {
            ImageContent(commonModifier)
        }
    }
}

@Composable
private fun ImageContent(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val kImageState by rememberKImageState(KImageType.Url("https://picsum.photos/500"))
        KImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F),
            contentScale = ContentScale.FillBounds,
            kImageState = kImageState,
        )
    }
}

@Composable
private fun QuantityChangerContent(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        var currentQuantity by remember { mutableIntStateOf(0) }
        QuantityChanger(
            currentQuantity = currentQuantity,
            quantityTransformer = { "$it Box" },
            onQuantityChange = {
                currentQuantity = it
            },
            quantityChangerConfig = QuantityChangerConfig.Default.copy(
                textStyle = QuantityChangerConfig.Default.textStyle.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            ),
        )
    }
}

@Composable
private fun AmountTextContent(modifier: Modifier) {
    CompositionLocalProvider(
        AmountTextConfigLocal provides AmountTextConfig.Default.copy(
            symbolRatio = 0.8,
            decimalRatio = 0.2,
            symbolFontWeightDiff = 3,
            decimalFontWeightDiff = 3,
        )
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            var amount by remember { mutableStateOf(Random.nextDouble(10.0, 10000.0)) }
            AmountText(
                amount = amount,
                style = LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                )
            )
            Button(
                onClick = {
                    amount = Random.nextDouble(10.0, 10000.0)
                },
            ) {
                Text("Random")
            }

        }
    }
}