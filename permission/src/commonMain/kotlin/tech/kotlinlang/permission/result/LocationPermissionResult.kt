package tech.kotlinlang.permission.result

sealed interface LocationPermissionResult {
    data object Granted: LocationPermissionResult
    data object NotAllowed: LocationPermissionResult
    data object Denied: LocationPermissionResult
}