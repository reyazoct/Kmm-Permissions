package tech.kotlinlang.permission

import platform.CoreLocation.CLLocationManager

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

    private fun checkLocationPermission(): LocationGrantedType {
        return locationManager.checkLocationPermission(CLLocationManager.authorizationStatus())
    }

    private suspend fun requestLocationPermission(): LocationGrantedType {
        return locationManager.askForLocationPermission()
    }
}