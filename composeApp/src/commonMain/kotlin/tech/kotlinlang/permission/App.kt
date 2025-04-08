package tech.kotlinlang.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.kotlinlang.permission.result.LocationPermissionResult

@Composable
@Preview
fun App() {
    MaterialTheme {
        var locationPermissionResult by remember {
            mutableStateOf<LocationPermissionResult>(
                LocationPermissionResult.Denied
            )
        }

        val scope = rememberCoroutineScope()
        val permissionHelper = remember { getPermissionHelper() }
        LaunchedEffect(Unit) {
            scope.launch {
                locationPermissionResult =
                    permissionHelper.checkIsPermissionGranted(Permission.Location)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (locationPermissionResult) {
                LocationPermissionResult.Denied -> {
                    Text("Location Permission is not allowed yet")
                    Button(
                        onClick = {
                            scope.launch {
                                locationPermissionResult =
                                    permissionHelper.requestForPermission(Permission.Location)
                            }
                        },
                    ) {
                        Text("Allow Permission")
                    }
                }

                LocationPermissionResult.NotAllowed -> {
                    Text("Location Permission is Not Allowed")
                }

                LocationPermissionResult.Granted.Approximate -> {
                    Text("Approximate Location Permission is Granted")
                }

                LocationPermissionResult.Granted.Precise -> {
                    Text("Precise Location Permission is Granted")
                }
            }
        }
    }
}