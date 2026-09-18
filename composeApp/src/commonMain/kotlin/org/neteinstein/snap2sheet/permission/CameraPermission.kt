package org.neteinstein.snap2sheet.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class PermissionStatus { NotDetermined, Granted, Denied }

/** Holds the live camera permission status and lets the UI trigger the platform's own request. */
@Stable
class CameraPermissionState(initialStatus: PermissionStatus) {
    var status: PermissionStatus by mutableStateOf(initialStatus)
        internal set

    internal var onRequest: () -> Unit = {}

    fun request() = onRequest()
}

/** Reads the OS camera permission and wires up the real system prompt when [CameraPermissionState.request] is called. */
@Composable
expect fun rememberCameraPermissionState(): CameraPermissionState
