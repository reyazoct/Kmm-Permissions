package tech.kotlinlang.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.annotation.OptIn
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import tech.kotlinlang.camera.analyser.AndroidImageAnalyser
import tech.kotlinlang.camera.analyser.ImageAnalyser
import java.util.concurrent.Executors
import kotlin.coroutines.resume

class AndroidCameraHelper : CameraHelper {

    private var cameraControl: CameraControl? = null
    private var imageCapture: ImageCapture? = null

    @OptIn(ExperimentalGetImage::class)
    @Composable
    override fun <T> CameraContent(
        modifier: Modifier,
        imageAnalyser: ImageAnalyser<T>,
    ) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val previewView = remember {
            PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        }

        AndroidView(
            modifier = modifier
                .clipToBounds(),
            factory = {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    val androidImageAnalyser = imageAnalyser as AndroidImageAnalyser<T>
                    val imageAnalyzer = ImageAnalysis.Builder().build().also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                            androidImageAnalyser.setImageProxy(imageProxy)
                            androidImageAnalyser.setStopListener {
                                it.clearAnalyzer()
                                cameraProvider.unbindAll()
                            }
                        }
                    }

                    imageCapture = ImageCapture
                        .Builder()
                        .build()

                    try {
                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture,
                            imageAnalyzer,
                        )
                        cameraControl = camera.cameraControl
                        preview.surfaceProvider = previewView.surfaceProvider
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(context))
                previewView
            },
            onRelease = {
                cameraControl = null
                imageCapture = null
            }
        )
    }

    override fun enableFlash(switchOn: Boolean) {
        cameraControl?.enableTorch(switchOn)
    }

    override suspend fun captureImage(): ImageBitmap {
        return suspendCancellableCoroutine { continuation ->
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val currentImageCapture = imageCapture
            currentImageCapture?.takePicture(
                cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.remaining())
                        buffer.get(bytes)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        val rotationDegrees = image.imageInfo.rotationDegrees
                        val correctedBitmap = if (rotationDegrees != 0) {
                            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                        } else {
                            bitmap
                        }
                        continuation.resume(correctedBitmap.asImageBitmap())
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        continuation.cancel(exception)
                    }
                }
            )
        }
    }
}