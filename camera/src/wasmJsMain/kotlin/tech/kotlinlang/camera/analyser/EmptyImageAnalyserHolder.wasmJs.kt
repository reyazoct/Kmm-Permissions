package tech.kotlinlang.camera.analyser

actual fun getEmptyImageAnalyser(): ImageAnalyser<Unit> {
    return object: ImageAnalyser<Unit> {
        override fun setListener(callback: (Unit) -> Unit) {

        }
    }
}