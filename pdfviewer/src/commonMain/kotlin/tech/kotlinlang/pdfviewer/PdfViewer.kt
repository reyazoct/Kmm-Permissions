package tech.kotlinlang.pdfviewer

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PdfViewer(
    modifier: Modifier = Modifier,
    pdfViewerState: PdfViewerState
) {
    if (pdfViewerState is PdfViewerState.Success) {
        PdfViewerLoaded(
            modifier = modifier,
            bytes = pdfViewerState.bytes,
        )
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            if (pdfViewerState is PdfViewerState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
internal expect fun PdfViewerLoaded(modifier: Modifier = Modifier, bytes: ByteArray)