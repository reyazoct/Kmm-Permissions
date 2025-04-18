package tech.kotlinlang.permission

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVCaptureDevice
import platform.CoreLocation.CLLocationManager
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import tech.kotlinlang.permission.result.LocationPermissionResult
import platform.UserNotifications.UNUserNotificationCenter
import tech.kotlinlang.permission.result.CameraPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import kotlin.coroutines.resume

class IosPermissionHelper : PermissionHelper {

    private val locationManager by lazy { IosLocationManager() }

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

    private suspend fun checkCameraPermission(): CameraPermissionResult {
        return suspendCancellableCoroutine { continuation ->
            checkCameraPermission { continuation.resume(it) }
        }
    }

    private fun checkCameraPermission(callback: (CameraPermissionResult) -> Unit) {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        val result = when (status) {
            AVAuthorizationStatusAuthorized -> CameraPermissionResult.Granted
            AVAuthorizationStatusDenied -> CameraPermissionResult.NotAllowed
            else -> CameraPermissionResult.Denied
        }
        callback(result)
    }

    private suspend fun requestCameraPermission(): CameraPermissionResult {
        return suspendCancellableCoroutine { continuation ->
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                checkCameraPermission { continuation.resume(it) }
            }
        }
    }

    private fun checkLocationPermission(): LocationPermissionResult {
        return locationManager.checkLocationPermission(CLLocationManager.authorizationStatus())
    }

    private suspend fun requestLocationPermission(): LocationPermissionResult {
        return locationManager.askForLocationPermission()
    }

    private suspend fun checkNotificationPermission(): NotificationPermissionResult {
        return suspendCancellableCoroutine { continuation ->
            checkNotificationPermission { continuation.resume(it) }
        }
    }

    private fun checkNotificationPermission(callback: (NotificationPermissionResult) -> Unit) {
        UNUserNotificationCenter.currentNotificationCenter().getNotificationSettingsWithCompletionHandler { settings ->
            val status = when (settings?.authorizationStatus) {
                UNAuthorizationStatusAuthorized -> NotificationPermissionResult.Granted
                UNAuthorizationStatusDenied -> NotificationPermissionResult.NotAllowed
                else -> NotificationPermissionResult.Denied
            }
            callback(status)
        }
    }

    private suspend fun requestNotificationPermission(): NotificationPermissionResult {
        val options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        return suspendCancellableCoroutine { continuation ->
            UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(options = options) { granted, error ->
                checkNotificationPermission { continuation.resume(it) }
            }
        }
    }
}