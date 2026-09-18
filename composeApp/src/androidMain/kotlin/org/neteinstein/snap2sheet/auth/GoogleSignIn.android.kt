package org.neteinstein.snap2sheet.auth

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import org.neteinstein.snap2sheet.domain.model.GoogleAccount

@Composable
actual fun rememberGoogleSignInController(): GoogleSignInController {
    val context = LocalContext.current
    return remember { AndroidGoogleSignInController(context) }
}

private class AndroidGoogleSignInController(private val context: Context) : GoogleSignInController {
    override suspend fun signIn(): Result<GoogleAccount> {
        if (!GoogleAuthConfig.isWebClientIdConfigured) {
            return Result.failure(
                IllegalStateException(
                    "Google Sign-In isn't configured yet. Set GoogleAuthConfig.WEB_CLIENT_ID to your " +
                        "OAuth Web client ID from the Google Cloud Console.",
                ),
            )
        }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(GoogleAuthConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val response = CredentialManager.create(context).getCredential(context, request)
            val idTokenCredential = GoogleIdTokenCredential.createFrom(response.credential.data)
            Result.success(
                GoogleAccount(
                    email = idTokenCredential.id,
                    initials = GoogleAccount.initialsFrom(idTokenCredential.displayName, idTokenCredential.id),
                ),
            )
        } catch (e: GetCredentialException) {
            Result.failure(IllegalStateException(e.message ?: "Google Sign-In was cancelled or failed.", e))
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(IllegalStateException("Couldn't read the Google account details.", e))
        }
    }
}
