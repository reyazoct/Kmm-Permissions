package tech.kotlinlang.permission

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CompletableDeferred

class PermissionInitiation private constructor() {

    private var activity: Activity? = null
    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private var permissionResult: CompletableDeferred<Boolean>? = null

    fun getActivity(): Activity {
        return activity ?: throw IllegalStateException("Activity not set")
    }

    private fun setActivity(activity: ComponentActivity) {
        this.activity = activity
        permissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            permissionResult?.complete(it)
        }
    }

    internal suspend fun requestPermission(permission: String): Boolean {
        val newPermissionResult = CompletableDeferred<Boolean>()
        permissionResult = newPermissionResult
        permissionLauncher?.launch(permission)
        return newPermissionResult.await()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var permissionInitiation: PermissionInitiation? = null

        fun setActivity(activity: ComponentActivity) {
            val currentPermissionInitiation = permissionInitiation
            val obj = currentPermissionInitiation ?: PermissionInitiation().also {
                permissionInitiation = it
            }
            obj.setActivity(activity)
        }

        fun clear() {
            permissionInitiation?.activity = null
            permissionInitiation = null
            HelperHolder.clear()
        }

        internal fun getInstance(): PermissionInitiation {
            return permissionInitiation
                ?: throw IllegalStateException("PermissionInitiation not initialized")
        }
    }
}