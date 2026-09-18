package org.neteinstein.snap2sheet.auth

import androidx.compose.runtime.Composable
import org.neteinstein.snap2sheet.domain.model.GoogleAccount

/** Drives the platform's native Google Sign-In UI and returns the resulting account. */
interface GoogleSignInController {
    suspend fun signIn(): Result<GoogleAccount>
}

@Composable
expect fun rememberGoogleSignInController(): GoogleSignInController
