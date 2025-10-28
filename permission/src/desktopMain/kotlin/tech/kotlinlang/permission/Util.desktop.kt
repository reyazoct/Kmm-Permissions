package tech.kotlinlang.permission

import tech.kotlinlang.permission.location.DesktopIPLocationHelper
import tech.kotlinlang.permission.location.IPApiLocationProvider
import tech.kotlinlang.permission.location.LocationHelper

@Deprecated(
    message = "Use HelperHolder.getPermissionHelperInstance() instead",
    replaceWith = ReplaceWith(expression = "HelperHolder.getPermissionHelperInstance()"),
    level = DeprecationLevel.WARNING
)
actual fun getPermissionHelper(): PermissionHelper = DesktopPermissionHelper()

actual fun getLocationHelper(): LocationHelper = DesktopIPLocationHelper(IPApiLocationProvider)
