package tech.kotlinlang.permission

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import tech.kotlinlang.permission.result.LocationPermissionResult
import kotlin.coroutines.resume

class IosLocationManager : NSObject(), CLLocationManagerDelegateProtocol {

    private val locationManager by lazy { CLLocationManager() }

    private var locationPermissionContinuation: (CancellableContinuation<LocationPermissionResult>)? =
        null

    init {
        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }

    suspend fun askForLocationPermission(): LocationPermissionResult {
        return suspendCancellableCoroutine { continuation ->
            locationPermissionContinuation = continuation
            locationManager.requestWhenInUseAuthorization()
        }
    }

    override fun locationManager(
        manager: CLLocationManager,
        didChangeAuthorizationStatus: CLAuthorizationStatus
    ) {
        if (locationPermissionContinuation?.isActive != true) return
        locationPermissionContinuation?.resume(checkLocationPermission(didChangeAuthorizationStatus))
        locationPermissionContinuation = null
    }

    fun checkLocationPermission(status: CLAuthorizationStatus): LocationPermissionResult {
        return when (status) {
            kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> {
                if (locationManager.accuracyAuthorization.value == 1L) LocationPermissionResult.Granted.Approximate
                else LocationPermissionResult.Granted.Precise
            }
            kCLAuthorizationStatusRestricted, kCLAuthorizationStatusDenied -> LocationPermissionResult.NotAllowed
            else -> LocationPermissionResult.Denied
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        if (locationPermissionContinuation?.isActive != true) return
        locationPermissionContinuation?.resume(LocationPermissionResult.Denied)
        locationPermissionContinuation = null
    }
}