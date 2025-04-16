package tech.kotlinlang.permission.result

sealed interface NotificationPermissionResult {
    data object Granted: NotificationPermissionResult
    data object NotAllowed: NotificationPermissionResult
    data object Denied: NotificationPermissionResult
}