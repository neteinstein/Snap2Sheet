package org.neteinstein.snap2sheet.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberCameraPermissionState(): CameraPermissionState {
    val state = remember { CameraPermissionState(currentCameraPermissionStatus()) }
    state.onRequest = {
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            dispatch_async(dispatch_get_main_queue()) {
                state.status = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
            }
        }
    }
    return state
}

@OptIn(ExperimentalForeignApi::class)
private fun currentCameraPermissionStatus(): PermissionStatus =
    when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
        AVAuthorizationStatusAuthorized -> PermissionStatus.Granted
        AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> PermissionStatus.Denied
        AVAuthorizationStatusNotDetermined -> PermissionStatus.NotDetermined
        else -> PermissionStatus.NotDetermined
    }
