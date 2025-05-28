package tech.kotlinlang.camera.qr

import tech.kotlinlang.camera.analyser.ImageAnalyser

actual fun getQrCodeImageAnalyser(): ImageAnalyser<String> {
    return object: ImageAnalyser<String> {
        override fun setListener(callback: (String) -> Unit) {
        }
    }
}