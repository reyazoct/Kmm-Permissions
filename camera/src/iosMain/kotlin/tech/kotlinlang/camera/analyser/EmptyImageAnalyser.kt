package tech.kotlinlang.camera.analyser

import platform.AVFoundation.AVCaptureOutput

class EmptyImageAnalyser : IosImageAnalyser<Unit> {
    override fun provideAVCaptureOutput(): AVCaptureOutput? {
        return null
    }

    override fun initializeAVCaptureOutput(output: AVCaptureOutput) {}
    override fun setListener(callback: (Unit) -> Unit) {}
}