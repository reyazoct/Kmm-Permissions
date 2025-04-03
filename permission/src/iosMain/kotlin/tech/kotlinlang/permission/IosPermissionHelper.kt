package tech.kotlinlang.permission

import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import tech.kotlinlang.permission.result.LocationPermissionResult

class IosPermissionHelper: PermissionHelper {

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
        return when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> LocationPermissionResult.Granted
            kCLAuthorizationStatusRestricted, kCLAuthorizationStatusDenied -> LocationPermissionResult.NotAllowed
            else -> LocationPermissionResult.Denied
        }
    }

    private suspend fun requestLocationPermission(): LocationPermissionResult {
        return locationManager.askForLocationPermission()
    }
}