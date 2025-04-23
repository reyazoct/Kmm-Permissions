package tech.kotlinlang.camera.analyser

import androidx.camera.core.ImageProxy

class EmptyImageAnalyser: AndroidImageAnalyser<Unit> {
    override fun setImageProxy(imageProxy: ImageProxy) {}
    override fun setListener(callback: (Unit) -> Unit) {}
    override fun setStopListener(callback: () -> Unit) {}
}