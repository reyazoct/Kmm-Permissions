package tech.kotlinlang.camera

actual fun getCameraHelper(): CameraHelper {
    return WasmCameraHelper()
}