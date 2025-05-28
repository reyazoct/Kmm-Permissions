package tech.kotlinlang.permission

import kotlinx.coroutines.await
import tech.kotlinlang.permission.result.CameraPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult
import kotlin.js.Promise

private fun cameraPermissionQuery(): Promise<JsAny> = js("navigator.permissions.query({ name: 'camera' })")

private fun askCameraPermission(): Promise<JsAny> = js("navigator.mediaDevices.getUserMedia({ video: true })")

private fun notificationPermissionQuery(): String = js("Notification.permission")

private fun askNotificationPermission(): Promise<JsString> = js("Notification.requestPermission()")


external interface StatusData : JsAny {
    val state: String
}

class WasmJsPermissionHelper : PermissionHelper {
    override suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T {
        return when (permission) {
            Permission.Camera -> checkCameraPermission()
            Permission.Notification -> checkNotificationPermission()
            else -> TODO("Not yet implemented")
        } as T
    }

    override suspend fun <T> requestForPermission(permission: Permission<T>): T {
        return when (permission) {
            Permission.Camera -> requestCameraPermission()
            Permission.Notification -> requestNotificationPermission()
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
}