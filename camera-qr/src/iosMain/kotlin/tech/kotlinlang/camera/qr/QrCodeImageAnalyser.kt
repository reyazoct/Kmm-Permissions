package tech.kotlinlang.camera.qr

import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObject
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import tech.kotlinlang.camera.analyser.IosImageAnalyser

class QrCodeImageAnalyser : IosImageAnalyser<String> {
    private var scanningCallback: ((String) -> Unit)? = null

    override fun provideAVCaptureOutput(): AVCaptureOutput? {
        return AVCaptureMetadataOutput()
    }

    override fun initializeAVCaptureOutput(output: AVCaptureOutput) {
        val metadataOutput = output as AVCaptureMetadataOutput
        val supportedTypes = metadataOutput.availableMetadataObjectTypes
        if (AVMetadataObjectTypeQRCode in supportedTypes) {
            metadataOutput.metadataObjectTypes = listOf(AVMetadataObjectTypeQRCode)
        }
        val delegate = object : NSObject(), AVCaptureMetadataOutputObjectsDelegateProtocol {
            override fun captureOutput(
                output: AVCaptureOutput,
                didOutputMetadataObjects: List<*>,
                fromConnection: AVCaptureConnection
            ) {
                val qrCode = didOutputMetadataObjects.firstOrNull {
                    it is AVMetadataObject && it.type == AVMetadataObjectTypeQRCode
                } as? AVMetadataMachineReadableCodeObject

                qrCode?.stringValue?.let { result ->
                    scanningCallback?.invoke(result)
                }
            }
        }
        metadataOutput.setMetadataObjectsDelegate(delegate, dispatch_get_main_queue())
    }

    override fun setListener(callback: (String) -> Unit) {
        this.scanningCallback = callback
    }
}