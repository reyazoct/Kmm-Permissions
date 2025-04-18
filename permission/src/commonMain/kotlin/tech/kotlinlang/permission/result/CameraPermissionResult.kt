package tech.kotlinlang.permission.result

sealed interface CameraPermissionResult {
    data object Granted: CameraPermissionResult
    data object NotAllowed: CameraPermissionResult
    data object Denied: CameraPermissionResult
}