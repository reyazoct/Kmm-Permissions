package tech.kotlinlang.camera.analyser

actual fun getEmptyImageAnalyser(): ImageAnalyser<Unit> {
    return EmptyImageAnalyser()
}