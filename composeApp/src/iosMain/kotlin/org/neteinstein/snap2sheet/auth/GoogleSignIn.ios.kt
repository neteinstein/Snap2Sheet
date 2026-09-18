package org.neteinstein.snap2sheet.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.suspendCancellableCoroutine
import org.neteinstein.snap2sheet.domain.model.GoogleAccount
import kotlin.coroutines.resume

/**
 * Bridges into the native `GIDSignIn` flow, which needs a presenting `UIViewController` that only
 * Swift/SwiftUI has. `iosApp/iosApp/GoogleSignInBridge.swift` implements this and registers itself
 * into [IosGoogleSignInProvider] on app launch.
 */
interface IosGoogleSignInBridge {
    fun signIn(onResult: (GoogleAccount?, String?) -> Unit)
}

object IosGoogleSignInProvider {
    var bridge: IosGoogleSignInBridge? = null
}

@Composable
actual fun rememberGoogleSignInController(): GoogleSignInController = remember { IosGoogleSignInController() }

private class IosGoogleSignInController : GoogleSignInController {
    override suspend fun signIn(): Result<GoogleAccount> {
        val bridge = IosGoogleSignInProvider.bridge
            ?: return Result.failure(
                IllegalStateException(
                    "Google Sign-In isn't configured yet. See iosApp/iosApp/GoogleSignInBridge.swift.",
                ),
            )

        return suspendCancellableCoroutine { continuation ->
            bridge.signIn { account, error ->
                if (account != null) {
                    continuation.resume(Result.success(account))
                } else {
                    continuation.resume(Result.failure(IllegalStateException(error ?: "Google Sign-In failed.")))
                }
            }
        }
    }
}
