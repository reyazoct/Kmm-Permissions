package tech.kotlinlang.permission.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onCameraClick: () -> Unit,
    onUiComponentsClick: () -> Unit,
    onPdfViewerClick: () -> Unit,
    onPermissionClick: () -> Unit,
) {
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
            Spacer(
                Modifier.height(12.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        }
        item {
            PermissionContent(
                modifier = commonModifier,
                onPermissionClick = onPermissionClick,
            )
        }
        item {
            CameraContent(
                modifier = commonModifier,
                onCameraClick = onCameraClick,
            )
        }
        item {
            UiContentContent(
                modifier = commonModifier,
                onUiComponentsClick = onUiComponentsClick,
            )
        }
        item {
            PdfViewerContent(
                modifier = commonModifier,
                onPdfViewerClick = onPdfViewerClick,
            )
        }
        item {
            Spacer(
                Modifier.height(12.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            )
        }
    }
}

@Composable
private fun UiContentContent(
    modifier: Modifier,
    onUiComponentsClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onUiComponentsClick,
        ) {
            Text("Goto UI Components")
        }
    }
}

@Composable
private fun PermissionContent(
    modifier: Modifier,
    onPermissionClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onPermissionClick,
        ) {
            Text("Goto Permission Screen")
        }
    }
}

@Composable
private fun PdfViewerContent(
    modifier: Modifier,
    onPdfViewerClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onPdfViewerClick,
        ) {
            Text("Goto UI PDF Viewer")
        }
    }
}

@Composable
private fun CameraContent(
    modifier: Modifier,
    onCameraClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onCameraClick,
        ) {
            Text("Goto Camera Screen")
        }
    }
}
