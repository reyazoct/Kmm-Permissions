package tech.kotlinlang.image

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.skia.Image

actual suspend fun ByteArray.toImageBitmap(): ImageBitmap? {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual val currentCoroutineDispatcher: CoroutineDispatcher
    get() = Dispatchers.Main