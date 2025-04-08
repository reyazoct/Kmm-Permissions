package tech.kotlinlang.permission

import platform.CoreLocation.CLLocationManager
import tech.kotlinlang.permission.result.LocationPermissionResult

class IosPermissionHelper : PermissionHelper {

    private val locationManager by lazy { IosLocationManager() }

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
        return locationManager.checkLocationPermission(CLLocationManager.authorizationStatus())
    }

    private suspend fun requestLocationPermission(): LocationPermissionResult {
        return locationManager.askForLocationPermission()
    }
}