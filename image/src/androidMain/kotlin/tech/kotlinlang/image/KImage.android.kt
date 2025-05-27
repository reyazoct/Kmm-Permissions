package tech.kotlinlang.image

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.File

actual suspend fun ByteArray.toImageBitmap(): ImageBitmap? {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}

actual val currentCoroutineDispatcher: CoroutineDispatcher
    get() = Dispatchers.IO