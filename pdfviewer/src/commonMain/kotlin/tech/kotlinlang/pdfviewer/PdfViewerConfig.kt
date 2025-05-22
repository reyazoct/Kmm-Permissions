package tech.kotlinlang.pdfviewer

import io.ktor.client.HttpClient

object PdfViewerConfig {
    private var httpClient: HttpClient? = null

    fun init(client: HttpClient) {
        httpClient = client
    }

    internal fun requireClient(): HttpClient {
        return httpClient ?: error("HttpClient is not initialized. Please call PdfViewerConfig.setHttpClient() in your app.")
    }
}
