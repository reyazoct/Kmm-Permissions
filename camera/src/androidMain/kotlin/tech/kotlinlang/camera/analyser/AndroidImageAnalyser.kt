package tech.kotlinlang.camera.analyser

import androidx.camera.core.ImageProxy

interface AndroidImageAnalyser<T>: ImageAnalyser<T> {
    fun setImageProxy(imageProxy: ImageProxy)
    fun setStopListener(callback: () -> Unit)
}