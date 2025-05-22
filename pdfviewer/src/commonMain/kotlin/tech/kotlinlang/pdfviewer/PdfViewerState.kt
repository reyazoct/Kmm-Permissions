package tech.kotlinlang.pdfviewer

sealed class PdfViewerState {
    object Loading : PdfViewerState()
    data class Success(val bytes: ByteArray) : PdfViewerState() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Success

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    data class Error(val message: String) : PdfViewerState()
}
