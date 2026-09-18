package org.neteinstein.snap2sheet.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.neteinstein.snap2sheet.domain.model.GoogleAccount

/**
 * The signed-in Google account. The actual sign-in happens through the platform's native Google
 * Sign-In flow (see `GoogleSignIn.kt`'s `rememberGoogleSignInController()`), which reports the
 * result back here via [setSignedIn].
 */
interface AccountRepository {
    val account: StateFlow<GoogleAccount?>
    fun setSignedIn(account: GoogleAccount)
    fun signOut()
}

class DefaultAccountRepository : AccountRepository {
    private val _account = MutableStateFlow<GoogleAccount?>(null)
    override val account: StateFlow<GoogleAccount?> = _account

    override fun setSignedIn(account: GoogleAccount) {
        _account.value = account
    }

    override fun signOut() {
        _account.value = null
    }
}
