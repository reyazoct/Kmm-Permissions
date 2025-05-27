import androidx.compose.ui.graphics.ImageBitmap
import tech.kotlinlang.image.KImageType

sealed class KImageState {
    data class Success(val imageBitmap: ImageBitmap) : KImageState()
    data object Loading: KImageState()
    data object Failed: KImageState()
}