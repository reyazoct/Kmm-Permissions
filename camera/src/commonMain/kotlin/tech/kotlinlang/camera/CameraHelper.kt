package tech.kotlinlang.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import tech.kotlinlang.camera.analyser.ImageAnalyser

interface CameraHelper {
    @Composable
    fun <T> CameraContent(modifier: Modifier, imageAnalyser: ImageAnalyser<T>)

    fun enableFlash(switchOn: Boolean)

    suspend fun captureImage(): ImageBitmap
}