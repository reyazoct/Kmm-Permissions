package tech.kotlinlang.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.kotlinlang.camera.getCameraHelper
import tech.kotlinlang.permission.location.LocationRequestResult
import tech.kotlinlang.permission.result.CameraPermissionResult
import tech.kotlinlang.permission.result.LocationPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult

@Composable
@Preview
fun App() {
    MaterialTheme {
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
                LocationPermissionContent(commonModifier)
            }
            item {
                LocationFetchContent(commonModifier)
            }
            item {
                NotificationPermissionContent(commonModifier)
            }
            item {
                CameraPermissionContent(commonModifier)
            }
            item {
                Spacer(
                    Modifier.height(12.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                )
            }
        }
    }
}

@Composable
private fun CameraPermissionContent(
    modifier: Modifier = Modifier
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
                val cameraHelper = remember { getCameraHelper() }
                Spacer(Modifier.height(12.dp))
                cameraHelper.CameraContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1F)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        }
    }
}


@Composable
private fun NotificationPermissionContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var notificationPermissionResult by remember {
            mutableStateOf<NotificationPermissionResult>(
                NotificationPermissionResult.Denied
            )
        }

        val scope = rememberCoroutineScope()
        val permissionHelper = remember { HelperHolder.getPermissionHelperInstance() }
        LaunchedEffect(Unit) {
            scope.launch {
                notificationPermissionResult = permissionHelper.checkIsPermissionGranted(Permission.Notification)
            }
        }

        when (notificationPermissionResult) {
            NotificationPermissionResult.Denied -> {
                Text("Notification Permission is not allowed yet")
                Button(
                    onClick = {
                        scope.launch {
                            notificationPermissionResult = permissionHelper.requestForPermission(Permission.Notification)
                        }
                    },
                ) {
                    Text("Allow Permission")
                }
            }

            NotificationPermissionResult.NotAllowed -> {
                Text("Notification Permission is Not Allowed")
            }

            NotificationPermissionResult.Granted -> {
                Text("Notification Permission is Granted")
            }
        }
    }
}

@Composable
private fun LocationFetchContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val scope = rememberCoroutineScope()
        val locationHelper = remember { HelperHolder.getLocationHelperInstance() }

        var locationRequestResult by remember {
            mutableStateOf<LocationRequestResult?>(null)
        }

        when (locationRequestResult) {
            is LocationRequestResult.LocationData -> {
                val currentResult = locationRequestResult as LocationRequestResult.LocationData
                Text("Fetched Last known location: ${currentResult.latitude}, ${currentResult.longitude}")
            }

            LocationRequestResult.NoLastLocationFound -> {
                Text("No last location found for device")
            }

            is LocationRequestResult.PermissionFailure -> {
                Text("Request permission first, Location permission not Granted")
            }

            null -> {
                Text("Fetch Last known location")
                Button(
                    onClick = {
                        scope.launch {
                            locationRequestResult = locationHelper.fetchLastKnownLocation()
                        }
                    },
                ) {
                    Text("Fetch")
                }
            }
        }
    }
}

@Composable
private fun LocationPermissionContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var locationPermissionResult by remember {
            mutableStateOf<LocationPermissionResult>(
                LocationPermissionResult.Denied
            )
        }

        val scope = rememberCoroutineScope()
        val permissionHelper = remember { HelperHolder.getPermissionHelperInstance() }
        LaunchedEffect(Unit) {
            scope.launch {
                locationPermissionResult =
                    permissionHelper.checkIsPermissionGranted(Permission.Location)
            }
        }

        when (locationPermissionResult) {
            LocationPermissionResult.Denied -> {
                Text("Location Permission is not allowed yet")
                Button(
                    onClick = {
                        scope.launch {
                            locationPermissionResult = permissionHelper.requestForPermission(Permission.Location)
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
