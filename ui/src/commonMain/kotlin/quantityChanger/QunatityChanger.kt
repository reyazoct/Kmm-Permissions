package quantityChanger

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuantityChanger(
    modifier: Modifier = Modifier,
    currentQuantity: Int,
    quantityTransformer: (Int) -> String,
    onQuantityChange: (Int) -> Unit,
    quantityChangerConfig: QuantityChangerConfig,
) {
    var previousQuantity by remember { mutableStateOf(currentQuantity) }
    val transitionSpecPair = remember {
        val animationDuration = 250
        val tweenSpecInt = tween<IntOffset>(durationMillis = animationDuration)
        val tweenSpecFloat = tween<Float>(durationMillis = animationDuration)

        val first = (slideInHorizontally(tweenSpecInt) { width -> width } + fadeIn(animationSpec = tweenSpecFloat))
            .togetherWith(slideOutHorizontally(tweenSpecInt) { width -> -width } + fadeOut(tweenSpecFloat))

        val second = (slideInHorizontally(tweenSpecInt) { width -> -width } + fadeIn(tweenSpecFloat))
            .togetherWith(slideOutHorizontally(tweenSpecInt) { width -> width } + fadeOut(tweenSpecFloat))

        first to second
    }
    val transitionSpec = remember(currentQuantity) {
        if (currentQuantity >= previousQuantity) {
            transitionSpecPair.first
        } else {
            transitionSpecPair.second
        }
    }
    val scope = rememberCoroutineScope()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(quantityChangerConfig.gap),
    ) {
        val previousIconShape = quantityChangerConfig.actionShape
        Icon(
            modifier = Modifier
                .background(
                    color = quantityChangerConfig.actionBackgroundColor,
                    shape = previousIconShape,
                )
                .border(
                    width = quantityChangerConfig.actionBorderWidth,
                    color = quantityChangerConfig.actionBorderColor,
                    shape = previousIconShape,
                ).clip(previousIconShape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            val job = scope.launch {
                                while (true) {
                                    onQuantityChange(previousQuantity - 1)
                                    delay(250)
                                }
                            }
                            tryAwaitRelease()
                            job.cancel()
                        }
                    )
                }
                .padding(
                    quantityChangerConfig.actionPaddingValues
                ),
            imageVector = quantityChangerConfig.prevIcon,
            contentDescription = null,
            tint = quantityChangerConfig.actionIconTint,
        )
        AnimatedContent(
            modifier = Modifier.background(
                color = quantityChangerConfig.centerBackgroundColor,
                shape = quantityChangerConfig.centerShape
            ).border(
                width = quantityChangerConfig.centerBorderWidth,
                color = quantityChangerConfig.centerBorderColor,
                shape = quantityChangerConfig.centerShape,
            ),
            targetState = currentQuantity,
            transitionSpec = { transitionSpec },
        ) { value ->
            Text(
                modifier = Modifier.padding(quantityChangerConfig.centerPaddingValues),
                text = quantityTransformer(value),
                style = quantityChangerConfig.textStyle,
            )
        }
        val nextIconShape = if (quantityChangerConfig.flipShape) quantityChangerConfig.actionShape
        else quantityChangerConfig.actionShape.flip()
        Icon(
            modifier = Modifier
                .background(
                    color = quantityChangerConfig.actionBackgroundColor,
                    shape = nextIconShape
                )
                .border(
                    width = quantityChangerConfig.actionBorderWidth,
                    color = quantityChangerConfig.actionBorderColor,
                    shape = nextIconShape,
                ).clip(nextIconShape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            val job = scope.launch {
                                while (true) {
                                    onQuantityChange(previousQuantity + 1)
                                    delay(250)
                                }
                            }
                            tryAwaitRelease()
                            job.cancel()
                        }
                    )
                }
                .padding(
                    quantityChangerConfig.actionPaddingValues
                ),
            imageVector = quantityChangerConfig.nextIcon,
            contentDescription = null,
            tint = quantityChangerConfig.actionIconTint,
        )
    }
    LaunchedEffect(currentQuantity) {
        previousQuantity = currentQuantity
    }
}

private fun Shape.flip(): Shape {
    return object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val layoutDirection = if (layoutDirection == LayoutDirection.Ltr) LayoutDirection.Rtl else LayoutDirection.Ltr
            return this@flip.createOutline(size, layoutDirection, density)
        }
    }
}