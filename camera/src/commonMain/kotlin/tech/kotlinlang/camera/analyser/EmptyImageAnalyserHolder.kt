package tech.kotlinlang.camera.analyser

class EmptyImageAnalyserHolder {
    companion object {
        private var instanceHolder: ImageAnalyser<Unit>? = null
        fun getInstance(): ImageAnalyser<Unit> {
            return instanceHolder ?: getEmptyImageAnalyser().also {
                instanceHolder = it
            }
        }
    }
}

internal expect fun getEmptyImageAnalyser(): ImageAnalyser<Unit>