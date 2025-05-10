package quantityChanger

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class QuantityChangerConfig(
    val textStyle: TextStyle,
    val centerShape: Shape,
    val centerBackgroundColor: Color,
    val centerBorderWidth: Dp,
    val centerBorderColor: Color,
    val centerPaddingValues: PaddingValues,
    val actionShape: Shape,
    val actionBackgroundColor: Color,
    val actionBorderWidth: Dp,
    val actionBorderColor: Color,
    val actionPaddingValues: PaddingValues,
    val actionIconTint: Color,
    val nextIcon: ImageVector,
    val prevIcon: ImageVector,
    val gap: Dp,
    val flipShape: Boolean
) {
    companion object {
        @get:ReadOnlyComposable
        val Default: QuantityChangerConfig
            @Composable
            get() = QuantityChangerConfig(
                textStyle = LocalTextStyle.current,
                centerShape = RoundedCornerShape(4.dp),
                centerBackgroundColor = Color.Transparent,
                centerBorderWidth = 1.dp,
                centerBorderColor = Color(0xFF7F56D9),
                centerPaddingValues = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                actionShape = CircleShape,
                actionBackgroundColor = Color(0xFF7F56D9),
                actionBorderWidth = 1.dp,
                actionBorderColor = Color(0xFF7F56D9),
                actionPaddingValues = PaddingValues(4.dp),
                actionIconTint = Color.White,
                nextIcon = Icons.Default.Add,
                prevIcon = Icons.Default.Delete,
                gap = 4.dp,
                flipShape = false,
            )
    }
}