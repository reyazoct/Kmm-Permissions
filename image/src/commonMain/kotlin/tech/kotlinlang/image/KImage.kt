package tech.kotlinlang.image

import KImageState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@Composable
fun KImage(
    modifier: Modifier,
    contentScale: ContentScale,
    kImageState: KImageState,
) {
    when (kImageState) {
        KImageState.Failed -> {
            Box(modifier = modifier)
        }

        KImageState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is KImageState.Success -> {
            Image(
                modifier = modifier,
                contentScale = contentScale,
                contentDescription = null,
                bitmap = kImageState.imageBitmap,
            )
        }
    }
}

@Composable
fun rememberKImageState(kImageType: KImageType): State<KImageState> {
    val kImageState = remember(kImageType) { mutableStateOf<KImageState>(KImageState.Loading) }
    LaunchedEffect(kImageType) {
        kImageState.value = when (kImageType) {
            is KImageType.Url -> {
                val imageBitmap = downloadImage(kImageType.url)
                if (imageBitmap == null) {
                    KImageState.Failed
                } else {
                    KImageState.Success(imageBitmap)
                }
            }
        }
    }
    return kImageState
}

internal suspend fun downloadImage(url: String): ImageBitmap? {
    return withContext(currentCoroutineDispatcher) {
        return@withContext storedMap.getOrElse(url) {
            val client = HttpClient()
            return@withContext try {
                val response = client.get(url)
                response.body<ByteArray>().toImageBitmap()
            } catch (_: Exception) {
                null
            }?.also { storedMap.put(url, it) }
        }
    }
}

private val storedMap = mutableMapOf<String, ImageBitmap>()

internal expect suspend fun ByteArray.toImageBitmap(): ImageBitmap?

internal expect val currentCoroutineDispatcher: CoroutineDispatcher

val KImageConfigLocal = staticCompositionLocalOf<KImageConfig> { object : KImageConfig {} }