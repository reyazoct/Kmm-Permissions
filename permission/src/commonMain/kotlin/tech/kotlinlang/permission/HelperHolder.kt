package tech.kotlinlang.permission

import tech.kotlinlang.permission.location.LocationHelper

class HelperHolder {
    companion object {
        private var permissionHelper: PermissionHelper? = null
        fun getPermissionHelperInstance(): PermissionHelper {
            @Suppress("DEPRECATION")
            return permissionHelper ?: getPermissionHelper().also {
                permissionHelper = it
            }
        }

        private var locationHelper: LocationHelper? = null
        fun getLocationHelperInstance(): LocationHelper {
            return locationHelper ?: getLocationHelper().also {
                locationHelper = it
            }
        }

        internal fun clear() {
            permissionHelper = null
            locationHelper = null
        }
    }
}