package tech.kotlinlang.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import tech.kotlinlang.camera.analyser.ImageAnalyser
import tech.kotlinlang.camera.analyser.IosImageAnalyser
import platform.AVFoundation.torchAvailable
import platform.AVFoundation.setTorchModeOnWithLevel
import platform.AVFoundation.torchMode
import platform.AVFoundation.AVCaptureTorchModeOff

class IosCameraHelper : CameraHelper {
    @Composable
    @OptIn(ExperimentalForeignApi::class)
    override fun <T> CameraContent(
        modifier: Modifier,
        imageAnalyser: ImageAnalyser<T>,
    ) {
        val previewView = remember {
            UIView(frame = UIScreen.mainScreen.bounds).apply {
                clipsToBounds = true
                layer.masksToBounds = true
            }
        }

        DisposableEffect(Unit) {
            val captureSession = AVCaptureSession()
            val captureDevice = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)

            val input = AVCaptureDeviceInput.deviceInputWithDevice(captureDevice!!, null)
            if (input != null && captureSession.canAddInput(input)) {
                captureSession.addInput(input)
            }

            val iosImageAnalyser = imageAnalyser as IosImageAnalyser<T>
            val output = iosImageAnalyser.provideAVCaptureOutput()
            if (output != null && captureSession.canAddOutput(output)) {
                captureSession.addOutput(output)
                iosImageAnalyser.initializeAVCaptureOutput(output)
            }

            val videoPreviewLayer = AVCaptureVideoPreviewLayer(session = captureSession).apply {
                frame = previewView.bounds
                videoGravity = AVLayerVideoGravityResizeAspectFill
            }
            previewView.layer.addSublayer(videoPreviewLayer)
            previewView.autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
            captureSession.startRunning()

            onDispose {
                captureSession.stopRunning()
                videoPreviewLayer.removeFromSuperlayer()
            }
        }

        UIKitView(
            modifier = modifier.clipToBounds(),
            factory = { previewView },
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun enableFlash(switchOn: Boolean) {
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        if (device?.torchAvailable == true) {
            device.lockForConfiguration(null)
            if (switchOn) {
                device.setTorchModeOnWithLevel(1.0F, null)
            } else {
                device.torchMode = AVCaptureTorchModeOff
            }
            device.unlockForConfiguration()
        }
    }
}