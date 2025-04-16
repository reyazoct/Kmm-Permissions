package tech.kotlinlang.permission

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
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
import tech.kotlinlang.permission.location.LocationRequestResult
import kotlin.coroutines.resume

class IosLocationManager : NSObject(), CLLocationManagerDelegateProtocol {

    private val locationManager by lazy { CLLocationManager() }

    private var locationPermissionContinuation: (CancellableContinuation<LocationGrantedType>)? = null
    private var locationContinuation: (CancellableContinuation<LocationRequestResult>)? = null

    init {
        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }

    suspend fun askForLocationPermission(): LocationGrantedType {
        return suspendCancellableCoroutine { continuation ->
            locationPermissionContinuation = continuation
            locationManager.requestWhenInUseAuthorization()
        }
    }

    suspend fun fetchLocation(): LocationRequestResult {
        return suspendCancellableCoroutine { continuation ->
            locationContinuation = continuation
            locationManager.requestLocation()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        if (locationContinuation?.isActive != true) return
        val latitude: Double? = manager.location?.coordinate?.useContents { latitude }
        val longitude: Double? = manager.location?.coordinate?.useContents { longitude }
        if (latitude == null || longitude == null) {
            locationContinuation?.resume(LocationRequestResult.NoLastLocationFound)
        } else {
            locationContinuation?.resume(LocationRequestResult.LocationData(latitude, longitude))
        }
        locationContinuation = null
    }


    override fun locationManager(
        manager: CLLocationManager,
        didChangeAuthorizationStatus: CLAuthorizationStatus
    ) {
        if (locationPermissionContinuation?.isActive != true) return
        locationPermissionContinuation?.resume(checkLocationPermission(didChangeAuthorizationStatus))
        locationPermissionContinuation = null
    }

    fun checkLocationPermission(status: CLAuthorizationStatus): LocationGrantedType {
        return when (status) {
            kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> {
                if (locationManager.accuracyAuthorization.value == 1L) LocationGrantedType.GrantedType.Approximate
                else LocationGrantedType.GrantedType.Precise
            }

            kCLAuthorizationStatusRestricted, kCLAuthorizationStatusDenied -> LocationGrantedType.NotAllowed
            else -> LocationGrantedType.Denied
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        if (locationPermissionContinuation?.isActive == true) {
            locationPermissionContinuation?.resume(LocationGrantedType.Denied)
            locationPermissionContinuation = null
        }

        if (locationContinuation?.isActive == true) {
            locationContinuation?.resume(LocationRequestResult.NoLastLocationFound)
            locationContinuation = null
        }
    }
}