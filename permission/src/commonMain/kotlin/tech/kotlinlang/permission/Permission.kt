package tech.kotlinlang.permission

import tech.kotlinlang.permission.result.CameraPermissionResult
import tech.kotlinlang.permission.result.LocationPermissionResult
import tech.kotlinlang.permission.result.NotificationPermissionResult

sealed class Permission<T> {
    data object Location : Permission<LocationPermissionResult>()
    data object Notification : Permission<NotificationPermissionResult>()
    data object Camera : Permission<CameraPermissionResult>()
}