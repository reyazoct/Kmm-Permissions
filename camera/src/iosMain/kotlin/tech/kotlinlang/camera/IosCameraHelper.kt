package tech.kotlinlang.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureStillImageOutput
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.UIKit.UIScreen
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.AVFoundation.AVVideoCodecJPEG
import platform.AVFoundation.AVVideoCodecKey
import tech.kotlinlang.camera.analyser.ImageAnalyser
import tech.kotlinlang.camera.analyser.IosImageAnalyser
import platform.AVFoundation.torchAvailable
import platform.AVFoundation.setTorchModeOnWithLevel
import platform.AVFoundation.torchMode
import platform.AVFoundation.AVCaptureTorchModeOff
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreateCopyWithColorSpace
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.UIKit.UIImage
import kotlin.coroutines.resume

class IosCameraHelper : CameraHelper {

    private val photoOutput by lazy {
        AVCaptureStillImageOutput().also {
            it.outputSettings = mapOf(AVVideoCodecKey to AVVideoCodecJPEG)
        }
    }

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

            if (captureSession.canAddOutput(photoOutput)) {
                captureSession.addOutput(photoOutput)
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

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun captureImage(): ImageBitmap {
        return suspendCancellableCoroutine { continuation ->
            val connection = photoOutput.connectionWithMediaType(AVMediaTypeVideo)
            if (connection == null) {
                continuation.cancel(Throwable("AVCaptureConnection is not yet initialised"))
                return@suspendCancellableCoroutine
            }
            photoOutput.captureStillImageAsynchronouslyFromConnection(connection) { sampleBuffer, error ->
                if (error != null) {
                    continuation.cancel(Throwable("Error capturing still image: ${error.localizedDescription}"))
                    return@captureStillImageAsynchronouslyFromConnection
                }

                if (sampleBuffer == null) {
                    continuation.cancel(Throwable("Error: No sample buffer available"))
                    return@captureStillImageAsynchronouslyFromConnection
                }

                val imageData = AVCaptureStillImageOutput.jpegStillImageNSDataRepresentation(sampleBuffer)
                if (imageData == null) {
                    continuation.cancel(Throwable("Error: Failed to get JPEG data"))
                    return@captureStillImageAsynchronouslyFromConnection
                }

                val uiImage = UIImage.imageWithData(imageData)
                if (uiImage == null) {
                    continuation.cancel(Throwable("Error: Failed to create UIImage"))
                    return@captureStillImageAsynchronouslyFromConnection
                }

                val imageBitmap = uiImage.imageBitmap
                if (imageBitmap == null) {
                    continuation.cancel(Throwable("Failed to convert UIImage to ImageBitmap"))
                } else {
                    continuation.resume(imageBitmap)
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private val UIImage.imageBitmap: ImageBitmap?
        get() {
            val imageRef = CGImageCreateCopyWithColorSpace(this.CGImage, CGColorSpaceCreateDeviceRGB()) ?: return null

            val width = CGImageGetWidth(imageRef).toInt()
            val height = CGImageGetHeight(imageRef).toInt()

            val bytesPerRow = CGImageGetBytesPerRow(imageRef)
            val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
            val bytePointer = CFDataGetBytePtr(data)
            val length = CFDataGetLength(data)
            val alphaInfo = CGImageGetAlphaInfo(imageRef)

            val alphaType = when (alphaInfo) {
                CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst, CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL
                CGImageAlphaInfo.kCGImageAlphaFirst, CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL
                CGImageAlphaInfo.kCGImageAlphaNone, CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst, CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE
                else -> ColorAlphaType.UNKNOWN
            }

            val byteArray = ByteArray(length.toInt()) { index ->
                bytePointer!![index].toByte()
            }
            CFRelease(data)
            CFRelease(imageRef)

            val skiaImage = Image.makeRaster(
                imageInfo = ImageInfo(width = width, height = height, colorType = ColorType.RGBA_8888, alphaType = alphaType),
                bytes = byteArray,
                rowBytes = bytesPerRow.toInt(),
            )
            return skiaImage.toComposeImageBitmap()
        }
}