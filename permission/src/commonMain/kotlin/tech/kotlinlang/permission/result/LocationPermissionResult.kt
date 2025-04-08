package tech.kotlinlang.permission.result

sealed interface LocationPermissionResult {
    sealed interface Granted: LocationPermissionResult {
        data object Precise: Granted
        data object Approximate: Granted
    }
    data object NotAllowed: LocationPermissionResult
    data object Denied: LocationPermissionResult
}