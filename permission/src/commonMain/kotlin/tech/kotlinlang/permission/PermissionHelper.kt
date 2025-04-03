package tech.kotlinlang.permission

interface PermissionHelper {
    suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T
    suspend fun <T> requestForPermission(permission: Permission<T>): T
}