package tech.kotlinlang.permission

import tech.kotlinlang.permission.result.LocationPermissionResult

sealed class Permission<T> {
    data object Location: Permission<LocationPermissionResult>()
}