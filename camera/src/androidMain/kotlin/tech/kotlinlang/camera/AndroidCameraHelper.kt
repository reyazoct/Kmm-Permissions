package tech.kotlinlang.camera

import androidx.annotation.OptIn
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import tech.kotlinlang.camera.analyser.AndroidImageAnalyser
import tech.kotlinlang.camera.analyser.ImageAnalyser

class AndroidCameraHelper : CameraHelper {

    private var cameraControl: CameraControl? = null

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
        val imageCapture = remember {
            ImageCapture
                .Builder()
                .build()
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
            }
        )
    }

    override fun enableFlash(switchOn: Boolean) {
        cameraControl?.enableTorch(switchOn)
    }
}