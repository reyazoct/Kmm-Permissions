package tech.kotlinlang.pdfviewer

import androidx.compose.foundation.layout.Box
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
actual fun PdfViewerLoaded(modifier: Modifier, bytes: ByteArray) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "PDF viewer for Web\nis coming soon",
            style = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
            )
        )
    }
}