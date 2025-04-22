package tech.kotlinlang.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tech.kotlinlang.permission.result.CameraPermissionResult
import tech.kotlinlang.permission.result.LocationPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult
import androidx.core.content.edit

class AndroidPermissionHelper(
    private val permissionInitiation: PermissionInitiation
) : PermissionHelper {

    private val sharedPref by lazy {
        val activity = permissionInitiation.getActivity()
        activity.getSharedPreferences("PermissionPrefs", Context.MODE_PRIVATE)
    }

    override suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            Permission.Location -> checkLocationPermission()
            Permission.Notification -> checkNotificationPermission()
            Permission.Camera -> checkCameraPermission()
        } as T
    }

    override suspend fun <T> requestForPermission(permission: Permission<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            Permission.Location -> requestLocationPermission()
            Permission.Notification -> requestNotificationPermission()
            Permission.Camera -> requestCameraPermission()
        } as T
    }

    override fun openSettings() {
        val activity = permissionInitiation.getActivity()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }

    private fun checkCameraPermission(): CameraPermissionResult {
        val activity = permissionInitiation.getActivity()
        val isUsed = sharedPref.getBoolean("camera.permission", false)
        return if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            CameraPermissionResult.Granted
        } else if (isUsed && !ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA,
            )
        ) {
            CameraPermissionResult.NotAllowed
        } else {
            CameraPermissionResult.Denied
        }
    }

    private suspend fun requestCameraPermission(): CameraPermissionResult {
        val result = permissionInitiation.requestPermission(Manifest.permission.CAMERA)
        if (!result) {
            sharedPref.edit { putBoolean("camera.permission", true) }
        }
        return checkCameraPermission()
    }

    private fun checkNotificationPermission(): NotificationPermissionResult {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return NotificationPermissionResult.Granted
        }
        val activity = permissionInitiation.getActivity()
        val isUsed = sharedPref.getBoolean("notification.permission", false)
        return if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationPermissionResult.Granted
        } else if (isUsed && !ActivityCompat.shouldShowRequestPermissionRationale(
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
        val result = permissionInitiation.requestPermission(Manifest.permission.POST_NOTIFICATIONS)
        if (!result) {
            sharedPref.edit { putBoolean("notification.permission", true) }
        }
        return checkNotificationPermission()
    }

    private fun checkLocationPermission(): LocationPermissionResult {
        val activity = permissionInitiation.getActivity()
        val isUsed = sharedPref.getBoolean("location.permission", false)
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
        } else if (isUsed && !ActivityCompat.shouldShowRequestPermissionRationale(
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
        val result = permissionInitiation.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (!result) {
            sharedPref.edit { putBoolean("location.permission", true) }
        }
        return checkLocationPermission()
    }
}