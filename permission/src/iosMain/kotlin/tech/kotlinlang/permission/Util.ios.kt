package tech.kotlinlang.permission

import tech.kotlinlang.permission.location.IosLocationHelper
import tech.kotlinlang.permission.location.LocationHelper

actual fun getPermissionHelper(): PermissionHelper {
    return IosPermissionHelper()
}

actual fun getLocationHelper(): LocationHelper {
    return IosLocationHelper(HelperHolder.getPermissionHelperInstance())
}