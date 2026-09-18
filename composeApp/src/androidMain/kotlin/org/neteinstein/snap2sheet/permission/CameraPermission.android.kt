package org.neteinstein.snap2sheet.permission

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun rememberCameraPermissionState(): CameraPermissionState {
    val context = LocalContext.current
    val state = remember {
        CameraPermissionState(context.currentCameraPermissionStatus())
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        state.status = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
    }
    state.onRequest = { launcher.launch(Manifest.permission.CAMERA) }
    return state
}

private fun android.content.Context.currentCameraPermissionStatus(): PermissionStatus =
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        PermissionStatus.Granted
    } else {
        PermissionStatus.NotDetermined
    }
