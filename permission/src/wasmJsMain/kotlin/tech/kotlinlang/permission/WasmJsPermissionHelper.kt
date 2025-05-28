package tech.kotlinlang.permission

import kotlinx.coroutines.await
import tech.kotlinlang.permission.result.CameraPermissionResult
import kotlin.js.Promise

private val cameraPermissionQuery: Promise<JsAny> = js("navigator.permissions.query({ name: 'camera' })")

external interface StatusData : JsAny {
    val state: String
}

class WasmJsPermissionHelper : PermissionHelper {
    override suspend fun <T> checkIsPermissionGranted(permission: Permission<T>): T {
        return when (permission) {
            Permission.Camera -> checkCameraPermission()
            else -> TODO("Not yet implemented")
        } as T
    }

    override suspend fun <T> requestForPermission(permission: Permission<T>): T {
        TODO("Not yet implemented")
    }

    override fun openSettings() {
        TODO("Not yet implemented")
    }

    private suspend fun checkCameraPermission(): CameraPermissionResult {
        val statusData = cameraPermissionQuery.await<JsAny>().unsafeCast<StatusData>()
        return when (statusData.state) {
            "granted" -> CameraPermissionResult.Granted
            "denied" -> CameraPermissionResult.NotAllowed
            "prompt" -> CameraPermissionResult.Denied
            else -> throw IllegalStateException("Unknown state: ${statusData.state}")
        }
    }
}