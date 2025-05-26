package tech.kotlinlang.permission

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.kotlinlang.permission.ui.camera.CameraScreen
import tech.kotlinlang.permission.ui.component.PdfViewerScreen
import tech.kotlinlang.permission.ui.component.UiComponentsScreen
import tech.kotlinlang.permission.ui.home.HomeScreen
import tech.kotlinlang.permission.ui.permission.PermissionsScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf("home_screen") }
        when (currentScreen) {
            "home_screen" -> {
                HomeScreen(
                    onCameraClick = { currentScreen = "camera_screen" },
                    onUiComponentsClick = { currentScreen = "ui_components_screen" },
                    onPdfViewerClick = { currentScreen = "pdf_viewer_screen" },
                    onPermissionClick = { currentScreen = "permission_screen" },
                )
            }
            "permission_screen" -> {
                PermissionsScreen(
                    onCameraClick = { currentScreen = "camera_screen" },
                )
            }

            "camera_screen" -> CameraScreen()
            "ui_components_screen" -> UiComponentsScreen()
            "pdf_viewer_screen" -> PdfViewerScreen()
        }
    }
}
