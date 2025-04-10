package tech.kotlinlang.permission

import tech.kotlinlang.permission.location.LocationHelper

@Deprecated(
    message = "Use HelperHolder.getPermissionHelperInstance() instead",
    replaceWith = ReplaceWith("HelperHolder.getPermissionHelperInstance()"),
    level = DeprecationLevel.WARNING,
)
expect fun getPermissionHelper(): PermissionHelper

expect fun getLocationHelper(): LocationHelper