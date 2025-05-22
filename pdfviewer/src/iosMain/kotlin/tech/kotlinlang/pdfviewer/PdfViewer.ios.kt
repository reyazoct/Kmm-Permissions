package tech.kotlinlang.pdfviewer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.CoreGraphics.CGSizeMake
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.get
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageCreateCopyWithColorSpace
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.UIKit.UIImage
import platform.PDFKit.kPDFDisplayBoxMediaBox
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun PdfViewerLoaded(modifier: Modifier, bytes: ByteArray) {
    val pdfDocument = remember {
        val nsData = bytes.toNSData()
        PDFDocument(nsData)
    }

    LazyColumn(modifier = modifier) {
        items(pdfDocument.pageCount.toInt()) { index ->
            val page: PDFPage? = pdfDocument.pageAtIndex(index.toULong()) ?: return@items
            val bounds = page?.boundsForBox(kPDFDisplayBoxMediaBox) ?: return@items
            val width = CGRectGetWidth(bounds)
            val height = CGRectGetHeight(bounds)
            val size = CGSizeMake(width, height)
            val thumbnail: UIImage? = page.thumbnailOfSize(size, 0)
            val imageBitmap: ImageBitmap? = thumbnail?.imageBitmap

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "PDF Page $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
val UIImage.imageBitmap: ImageBitmap?
    get() {
        val imageRef = CGImageCreateCopyWithColorSpace(this.CGImage, CGColorSpaceCreateDeviceRGB()) ?: return null

        val width = CGImageGetWidth(imageRef).toInt()
        val height = CGImageGetHeight(imageRef).toInt()

        val bytesPerRow = CGImageGetBytesPerRow(imageRef)
        val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
        val bytePointer = CFDataGetBytePtr(data)
        val length = CFDataGetLength(data)
        val alphaInfo = CGImageGetAlphaInfo(imageRef)

        val alphaType = when (alphaInfo) {
            CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst, CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL
            CGImageAlphaInfo.kCGImageAlphaFirst, CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL
            CGImageAlphaInfo.kCGImageAlphaNone, CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst, CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE
            else -> ColorAlphaType.UNKNOWN
        }

        val byteArray = ByteArray(length.toInt()) { index ->
            bytePointer!![index].toByte()
        }
        CFRelease(data)
        CFRelease(imageRef)

        val skiaImage = Image.makeRaster(
            imageInfo = ImageInfo(width = width, height = height, colorType = ColorType.RGBA_8888, alphaType = alphaType),
            bytes = byteArray,
            rowBytes = bytesPerRow.toInt(),
        )
        return skiaImage.toComposeImageBitmap()
    }


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData = usePinned { pinned ->
    NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
}