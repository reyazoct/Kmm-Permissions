package tech.kotlinlang.permission.ui.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tech.kotlinlang.permission.HelperHolder
import tech.kotlinlang.permission.Permission
import tech.kotlinlang.permission.result.CameraPermissionResult

@Composable
actual fun PermissionsScreen(onCameraClick: () -> Unit) {
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
            CameraPermissionContent(
                modifier = commonModifier,
                onCameraClick = onCameraClick,
            )
        }
    }
}

@Composable
private fun CameraPermissionContent(
    modifier: Modifier = Modifier,
    onCameraClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var notificationPermissionResult by remember {
            mutableStateOf<CameraPermissionResult>(
                CameraPermissionResult.Denied
            )
        }

        val scope = rememberCoroutineScope()
        val permissionHelper = remember { HelperHolder.getPermissionHelperInstance() }
        LaunchedEffect(Unit) {
            scope.launch {
                notificationPermissionResult = permissionHelper.checkIsPermissionGranted(Permission.Camera)
            }
        }

        when (notificationPermissionResult) {
            CameraPermissionResult.Denied -> {
                Text("Camera Permission is not allowed yet")
                Button(
                    onClick = {
                        scope.launch {
                            notificationPermissionResult = permissionHelper.requestForPermission(Permission.Camera)
                        }
                    },
                ) {
                    Text("Allow Permission")
                }
            }

            CameraPermissionResult.NotAllowed -> {
                Text("Camera Permission is Not Allowed")
                Button(
                    onClick = { permissionHelper.openSettings() },
                ) {
                    Text("Go to Settings")
                }
            }

            CameraPermissionResult.Granted -> {
                Text("Camera Permission is Granted")
                Button(
                    onClick = onCameraClick,
                ) {
                    Text("Go to camera")
                }
            }
        }
    }
}
