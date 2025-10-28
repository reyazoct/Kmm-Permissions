package tech.kotlinlang.permission

import tech.kotlinlang.permission.result.CameraPermissionResult
import tech.kotlinlang.permission.result.LocationPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult
import tech.kotlinlang.permission.result.RecordAudioPermissionResult

class DesktopPermissionHelper : PermissionHelper {
    override suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T {
        // There's no permissions on desktop platforms, so we can assume all permissions are granted.
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            is Permission.Location -> LocationPermissionResult.Granted.Precise
            is Permission.Notification -> NotificationPermissionResult.Granted
            is Permission.Camera -> CameraPermissionResult.Granted
            is Permission.RecordAudio -> RecordAudioPermissionResult.Granted
        } as T
    }

    override suspend fun <T> requestForPermission(permission: Permission<T>): T {
        // There's no permissions on desktop platforms, so no need to request them.
        @Suppress("UNCHECKED_CAST")
        return when (permission) {
            is Permission.Location -> LocationPermissionResult.Granted.Precise
            is Permission.Notification -> NotificationPermissionResult.Granted
            is Permission.Camera -> CameraPermissionResult.Granted
            is Permission.RecordAudio -> RecordAudioPermissionResult.Granted
        } as T
    }

    override fun openSettings() {
        // There's no permissions on desktop platforms, so no settings to open.
    }
}
