package tech.kotlinlang.permission

import tech.kotlinlang.permission.location.LocationHelper
import tech.kotlinlang.permission.location.WasmJsLocationHelper

@Deprecated(
    message = "Use HelperHolder.getPermissionHelperInstance() instead",
    replaceWith = ReplaceWith(expression = "HelperHolder.getPermissionHelperInstance()"),
    level = DeprecationLevel.WARNING
)
actual fun getPermissionHelper(): PermissionHelper {
    return WasmJsPermissionHelper()
}

actual fun getLocationHelper(): LocationHelper {
    return WasmJsLocationHelper()
}