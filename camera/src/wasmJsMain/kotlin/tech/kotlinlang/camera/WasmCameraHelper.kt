package tech.kotlinlang.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import tech.kotlinlang.camera.analyser.ImageAnalyser

class WasmCameraHelper: CameraHelper {
    @Composable
    override fun <T> CameraContent(modifier: Modifier, imageAnalyser: ImageAnalyser<T>) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Camera Screen\nComing Soon!",
                style = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                )
            )
        }
    }

    override fun enableFlash(switchOn: Boolean) {
        throw NotImplementedError()
    }

    override suspend fun captureImage(): ImageBitmap {
        throw NotImplementedError()
    }
}