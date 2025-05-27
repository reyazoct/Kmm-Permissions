package tech.kotlinlang.image

import androidx.compose.ui.graphics.ImageBitmap
import platform.Foundation.NSData
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.get
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
import platform.Foundation.create
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual suspend fun ByteArray.toImageBitmap(): ImageBitmap? {
    val nsData = NSData.create(bytes = this.usePinned { it.addressOf(0) }, length = this.size.toULong())
    val uiImage = UIImage(data = nsData)
    return uiImage.imageBitmap
}

@OptIn(ExperimentalForeignApi::class)
private val UIImage.imageBitmap: ImageBitmap?
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

actual val currentCoroutineDispatcher: CoroutineDispatcher
    get() = Dispatchers.IO