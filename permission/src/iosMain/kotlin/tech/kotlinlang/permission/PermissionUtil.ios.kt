package tech.kotlinlang.permission

actual fun getPermissionHelper(): PermissionHelper {
    return IosPermissionHelper()
}