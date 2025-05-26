package tech.kotlinlang.permission.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tech.kotlinlang.pdfviewer.PdfViewer
import tech.kotlinlang.pdfviewer.rememberPdfViewerState

@Composable
actual fun PdfViewerScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val commonModifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            )
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)

        item {
            PdfViewerContent(commonModifier)
        }
    }
}

@Composable
private fun PdfViewerContent(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val pdfViewerState by rememberPdfViewerState("https://kotlinlang.org/docs/kotlin-reference.pdf")
        PdfViewer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F),
            pdfViewerState = pdfViewerState
        )
    }
}