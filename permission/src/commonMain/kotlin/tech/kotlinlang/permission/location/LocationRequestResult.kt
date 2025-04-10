package tech.kotlinlang.permission.location

import tech.kotlinlang.permission.result.LocationPermissionResult

sealed class LocationRequestResult {
    data class LocationData(
        val latitude: Double,
        val longitude: Double,
    ): LocationRequestResult()

    data class PermissionFailure(
        val locationPermissionResult: LocationPermissionResult
    ): LocationRequestResult()

    data object NoLastLocationFound: LocationRequestResult()
}