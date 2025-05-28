package tech.kotlinlang.permission.location

import kotlinx.coroutines.suspendCancellableCoroutine
import tech.kotlinlang.permission.Permission
import tech.kotlinlang.permission.PermissionHelper
import tech.kotlinlang.permission.getCurrentPosition
import tech.kotlinlang.permission.result.LocationPermissionResult
import kotlin.coroutines.resume

class WasmJsLocationHelper(
    private val permissionHelper: PermissionHelper
) : LocationHelper {
    override suspend fun fetchLastKnownLocation(): LocationRequestResult {
        val permissionResult = permissionHelper.checkIsPermissionGranted(Permission.Location)
        if (permissionResult !is LocationPermissionResult.Granted) {
            return LocationRequestResult.PermissionFailure(permissionResult)
        }
        return suspendCancellableCoroutine { continuation ->
            getCurrentPosition(
                { geolocationPosition ->
                    val locationData = LocationRequestResult.LocationData(geolocationPosition.coords.latitude, geolocationPosition.coords.longitude)
                    continuation.resume(locationData)
                },
                {
                    continuation.resume(LocationRequestResult.NoLastLocationFound)
                }
            )
        }
    }
}