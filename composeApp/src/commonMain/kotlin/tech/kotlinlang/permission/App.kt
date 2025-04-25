package tech.kotlinlang.permission

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.kotlinlang.permission.ui.camera.CameraScreen
import tech.kotlinlang.permission.ui.home.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf("home_screen") }
        when (currentScreen) {
            "home_screen" -> {
                HomeScreen(onCameraClick = { currentScreen = "camera_screen" })
            }

            "camera_screen" -> CameraScreen()
        }
    }
}
