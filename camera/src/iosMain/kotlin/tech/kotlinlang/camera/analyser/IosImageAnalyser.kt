package tech.kotlinlang.camera.analyser

import platform.AVFoundation.AVCaptureOutput

interface IosImageAnalyser<T>: ImageAnalyser<T> {
    fun provideAVCaptureOutput(): AVCaptureOutput?
    fun initializeAVCaptureOutput(output: AVCaptureOutput)
}