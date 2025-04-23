package tech.kotlinlang.camera.analyser

interface ImageAnalyser<T> {
    fun setListener(callback: (T) -> Unit)
}