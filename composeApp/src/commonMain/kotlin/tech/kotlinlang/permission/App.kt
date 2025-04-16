package tech.kotlinlang.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.kotlinlang.permission.location.LocationRequestResult
import tech.kotlinlang.permission.result.LocationPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LocationPermissionContent()
            Spacer(Modifier.height(24.dp))
            LocationFetchContent()
            Spacer(Modifier.height(24.dp))
            NotificationPermissionContent()
        }
    }
}

@Composable
private fun NotificationPermissionContent() {
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

@Composable
private fun LocationFetchContent() {
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

@Composable
private fun LocationPermissionContent() {
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