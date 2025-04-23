package tech.kotlinlang.camera.qr

import tech.kotlinlang.camera.analyser.ImageAnalyser

class QrCodeImageAnalyserHolder {
    companion object {
        private var instanceHolder: ImageAnalyser<String>? = null
        fun getInstance(): ImageAnalyser<String> {
            return instanceHolder ?: getQrCodeImageAnalyser().also {
                instanceHolder = it
            }
        }
    }
}

internal expect fun getQrCodeImageAnalyser(): ImageAnalyser<String>