package tech.kotlinlang.permission

import tech.kotlinlang.permission.location.AndroidLocationHelper
import tech.kotlinlang.permission.location.LocationHelper

actual fun getPermissionHelper(): PermissionHelper {
    return AndroidPermissionHelper(PermissionInitiation.getInstance())
}

actual fun getLocationHelper(): LocationHelper {
    return AndroidLocationHelper(
        permissionInitiation = PermissionInitiation.getInstance(),
        permissionHelper = HelperHolder.getPermissionHelperInstance(),
    )
}