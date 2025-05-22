package tech.kotlinlang.pdfviewer

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File

@Composable
internal actual fun PdfViewerLoaded(
    modifier: Modifier,
    bytes: ByteArray
) {
    val context = LocalContext.current
    var pdfRenderer by remember { mutableStateOf<PdfRenderer?>(null) }
    DisposableEffect(bytes) {
        val file = File.createTempFile("loaded_pdf", ".pdf", context.cacheDir)
        file.writeBytes(bytes)
        pdfRenderer = PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY))
        onDispose {
            pdfRenderer?.close()
            file.delete()
        }
    }
    LazyColumn(
        modifier = modifier,
    ) {
        val currentPdfRenderer = pdfRenderer ?: return@LazyColumn
        items(currentPdfRenderer.pageCount) { pageIndex ->
            AndroidView(
                modifier = modifier,
                factory = { context ->
                    val page = currentPdfRenderer.openPage(pageIndex)

                    val bitmap = createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                    val imageView = ImageView(context)
                    imageView.setImageBitmap(bitmap)

                    page.close()
                    imageView
                },
            )
        }
    }
}