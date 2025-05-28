package tech.kotlinlang.permission

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import tech.kotlinlang.permission.result.CameraPermissionResult
import tech.kotlinlang.permission.result.LocationPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult
import kotlin.js.Promise
import kotlin.coroutines.resume

private fun cameraPermissionQuery(): Promise<JsAny> = js("navigator.permissions.query({ name: 'camera' })")

private fun askCameraPermission(): Promise<JsAny> = js("navigator.mediaDevices.getUserMedia({ video: true })")

private fun notificationPermissionQuery(): String = js("Notification.permission")

private fun askNotificationPermission(): Promise<JsString> = js("Notification.requestPermission()")

private fun locationPermissionQuery(): Promise<JsAny> = js("navigator.permissions.query({ name: 'geolocation' })")

@JsFun(
    """
       (success, error) => {
           navigator.geolocation.getCurrentPosition(success, error)
       }
       """
)
external fun getCurrentPosition(success: (JsAny) -> Unit, error: (JsAny) -> Unit)

external interface StatusData : JsAny {
    val state: String
}

class WasmJsPermissionHelper : PermissionHelper {
    override suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T {
        return when (permission) {
            Permission.Camera -> checkCameraPermission()
            Permission.Notification -> checkNotificationPermission()
            Permission.Location -> checkLocationPermission()
            else -> TODO("Not yet implemented")
        } as T
    }

    override suspend fun <T> requestForPermission(permission: Permission<T>): T {
        return when (permission) {
            Permission.Camera -> requestCameraPermission()
            Permission.Notification -> requestNotificationPermission()
            Permission.Location -> requestLocationPermission()
            else -> TODO("Not yet implemented")
        } as T
    }

    override fun openSettings() {
    }

    private suspend fun requestCameraPermission(): CameraPermissionResult {
        try {
            askCameraPermission().await<JsAny>()
        } catch (_: Throwable) {
        }
        return checkCameraPermission()
    }

    private suspend fun checkCameraPermission(): CameraPermissionResult {
        val statusData = cameraPermissionQuery().await<JsAny>().unsafeCast<StatusData>()
        return when (statusData.state) {
            "granted" -> CameraPermissionResult.Granted
            "denied" -> CameraPermissionResult.NotAllowed
            "prompt" -> CameraPermissionResult.Denied
            else -> throw IllegalStateException("Unknown state: ${statusData.state}")
        }
    }

    private suspend fun requestNotificationPermission(): NotificationPermissionResult {
        try {
            askNotificationPermission().await<JsAny>()
        } catch (_: Throwable) {
        }
        return checkNotificationPermission()
    }

    private suspend fun checkNotificationPermission(): NotificationPermissionResult {
        val status = notificationPermissionQuery()
        return when (status) {
            "granted" -> NotificationPermissionResult.Granted
            "denied" -> NotificationPermissionResult.NotAllowed
            "default" -> NotificationPermissionResult.Denied
            else -> throw IllegalStateException("Unknown state: $status")
        }
    }

    private suspend fun checkLocationPermission(): LocationPermissionResult {
        val statusData = locationPermissionQuery().await<JsAny>().unsafeCast<StatusData>()
        return when (statusData.state) {
            "granted" -> LocationPermissionResult.Granted.Precise
            "denied" -> LocationPermissionResult.NotAllowed
            "prompt" -> LocationPermissionResult.Denied
            else -> throw IllegalStateException("Unknown state: ${statusData.state}")
        }
    }

    private suspend fun requestLocationPermission(): LocationPermissionResult {
        return suspendCancellableCoroutine { continuation ->
            getCurrentPosition(
                { jsAny ->
                    CoroutineScope(Dispatchers.Default).launch { continuation.resume(checkLocationPermission()) }
                }, { jsAny ->
                    CoroutineScope(Dispatchers.Default).launch { continuation.resume(checkLocationPermission()) }
                }
            )
        }
    }
}