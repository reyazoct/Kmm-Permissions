package tech.kotlinlang.permission.result

sealed interface RecordAudioPermissionResult {
    data object Granted: RecordAudioPermissionResult
    data object NotAllowed: RecordAudioPermissionResult
    data object Denied: RecordAudioPermissionResult
}