package tech.kotlinlang.permission.ui.permission

import androidx.compose.runtime.Composable

@Composable
expect fun PermissionsScreen(
    onCameraClick: () -> Unit,
)