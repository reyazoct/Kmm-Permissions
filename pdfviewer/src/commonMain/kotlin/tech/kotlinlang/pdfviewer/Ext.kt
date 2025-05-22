package tech.kotlinlang.pdfviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

@Composable
fun rememberPdfViewerState(url: String): State<PdfViewerState> {
    val state = remember { mutableStateOf<PdfViewerState>(PdfViewerState.Loading) }

    LaunchedEffect(url) {
        state.value = PdfViewerState.Loading
        val newPdfViewerState = downloadPdfFromUrl(url)
        state.value = newPdfViewerState
    }

    return state
}

internal suspend fun downloadPdfFromUrl(url: String): PdfViewerState {
    return withContext(Dispatchers.IO) {
        val client = PdfViewerConfig.requireClient()
        val response = client.get(url)
        return@withContext try {
            if (!response.status.isSuccess()) {
                PdfViewerState.Error("Failed to load PDF: ${response.status}")
            }
            PdfViewerState.Success(response.body())
        } catch (e: Exception) {
            PdfViewerState.Error("Failed to load PDF: ${e.message}")
        }
    }
}

