package tech.kotlinlang.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tech.kotlinlang.permission.result.LocationPermissionResult

class AndroidPermissionHelper(
    private val permissionInitiation: PermissionInitiation
) : PermissionHelper {

    override suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            Permission.Location -> checkLocationPermission()
        } as T
    }

    override suspend fun <T> requestForPermission(permission: Permission<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            Permission.Location -> requestLocationPermission()
        } as T
    }

    private fun checkLocationPermission(): LocationPermissionResult {
        val activity = permissionInitiation.getActivity()
        return if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationPermissionResult.Granted
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            LocationPermissionResult.NotAllowed
        } else {
            LocationPermissionResult.Denied
        }
    }

    private suspend fun requestLocationPermission(): LocationPermissionResult {
        val activity = permissionInitiation.getActivity()
        val result = permissionInitiation.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        return if (result) LocationPermissionResult.Granted
        else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationPermissionResult.NotAllowed
        } else {
            LocationPermissionResult.Denied
        }
    }
}