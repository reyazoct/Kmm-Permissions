package tech.kotlinlang.pdfviewer

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PdfViewer(pdfViewerState: PdfViewerState.Success) {
    PdfViewerLoaded(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F),
        bytes = pdfViewerState.bytes,
    )
}

@Composable
internal expect fun PdfViewerLoaded(modifier: Modifier = Modifier, bytes: ByteArray)