package tech.kotlinlang.permission.location

import tech.kotlinlang.permission.IosLocationManager
import tech.kotlinlang.permission.Permission
import tech.kotlinlang.permission.PermissionHelper

class IosLocationHelper(
    private val permissionHelper: PermissionHelper,
): LocationHelper {
    private val locationManager by lazy { IosLocationManager() }

    override suspend fun fetchLastKnownLocation(): LocationRequestResult {
        val permissionResult = permissionHelper.checkIsPermissionGranted(Permission.Location)
        if (permissionResult !is PermissionResult.Granted) {
            return LocationRequestResult.PermissionFailure(permissionResult)
        }
        return locationManager.fetchLocation()
    }
}