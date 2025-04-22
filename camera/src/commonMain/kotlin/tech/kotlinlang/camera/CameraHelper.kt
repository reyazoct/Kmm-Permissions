package tech.kotlinlang.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface CameraHelper {
    @Composable
    fun CameraContent(modifier: Modifier)
}