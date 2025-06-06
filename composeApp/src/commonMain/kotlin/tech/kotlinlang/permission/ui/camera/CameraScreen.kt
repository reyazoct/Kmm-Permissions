package tech.kotlinlang.permission.ui.camera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tech.kotlinlang.camera.getCameraHelper
import tech.kotlinlang.camera.qr.QrCodeImageAnalyserHolder

@Composable
fun CameraScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val cameraHelper = remember { getCameraHelper() }
        Spacer(Modifier.height(12.dp))

        val qrCodeImageAnalyser = remember { QrCodeImageAnalyserHolder.getInstance() }

        var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }
        var extractedString by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(Unit) {
            qrCodeImageAnalyser.setListener {
                extractedString = it
            }
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = extractedString != null
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(12.dp),
                text = "Extracted Data: $extractedString",
                style = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                ),
            )
        }
        val currentImage = capturedImage
        if (currentImage == null) {
            cameraHelper.CameraContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                imageAnalyser = qrCodeImageAnalyser
            )
        } else {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                bitmap = currentImage,
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val scope = rememberCoroutineScope()
            Button(
                onClick = {
                    scope.launch {
                        capturedImage = if (capturedImage == null) cameraHelper.captureImage()
                        else null
                    }
                }
            ) {
                Text(
                    text = if (capturedImage == null) "Capture Image" else "Go to camera"
                )
            }
            Spacer(Modifier.height(8.dp))
            AnimatedVisibility(capturedImage == null) {
                var flashOn by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        scope.launch {
                            cameraHelper.enableFlash(!flashOn)
                            flashOn = !flashOn
                        }
                    }
                ) {
                    Text(
                        text = if (flashOn) "Turn Off Flash" else "Turn On Flash"
                    )
                }
            }
        }
    }
}