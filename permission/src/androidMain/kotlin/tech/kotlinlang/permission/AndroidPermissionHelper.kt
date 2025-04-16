package tech.kotlinlang.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tech.kotlinlang.permission.result.LocationPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult

class AndroidPermissionHelper(
    private val permissionInitiation: PermissionInitiation
) : PermissionHelper {

    override suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            Permission.Location -> checkLocationPermission()
            Permission.Notification -> checkNotificationPermission()
        } as T
    }

    override suspend fun <T> requestForPermission(permission: Permission<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            Permission.Location -> requestLocationPermission()
            Permission.Notification -> requestNotificationPermission()
        } as T
    }

    private fun checkNotificationPermission(): NotificationPermissionResult {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return NotificationPermissionResult.Granted
        }
        val activity = permissionInitiation.getActivity()
        return if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationPermissionResult.Granted
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS,
            )
        ) {
            NotificationPermissionResult.NotAllowed
        } else {
            NotificationPermissionResult.Denied
        }
    }

    private suspend fun requestNotificationPermission(): NotificationPermissionResult {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return NotificationPermissionResult.Granted
        }
        permissionInitiation.requestPermission(Manifest.permission.POST_NOTIFICATIONS)
        return checkNotificationPermission()
    }

    private fun checkLocationPermission(): LocationPermissionResult {
        val activity = permissionInitiation.getActivity()
        return if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationPermissionResult.Granted.Precise
        } else if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationPermissionResult.Granted.Approximate
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            LocationPermissionResult.NotAllowed
        } else {
            LocationPermissionResult.Denied
        }
    }

    private suspend fun requestLocationPermission(): LocationPermissionResult {
        permissionInitiation.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        return checkLocationPermission()
    }
}