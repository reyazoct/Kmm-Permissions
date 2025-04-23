package tech.kotlinlang.camera.qr

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import tech.kotlinlang.camera.analyser.AndroidImageAnalyser
import kotlin.getValue

class QrCodeImageAnalyser : AndroidImageAnalyser<String> {
    private val barcodeScanner by lazy { BarcodeScanning.getClient() }
    private var scanningCallback: ((String) -> Unit)? = null
    private var stopCallback: (() -> Unit)? = null

    @OptIn(ExperimentalGetImage::class)
    override fun setImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image).addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { result ->
                        scanningCallback?.invoke(result)
                        stopCallback?.invoke()
                        return@addOnSuccessListener
                    }
                }
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }

    override fun setStopListener(callback: () -> Unit) {
        this.stopCallback = callback
    }

    override fun setListener(callback: (String) -> Unit) {
        this.scanningCallback = callback
    }
}