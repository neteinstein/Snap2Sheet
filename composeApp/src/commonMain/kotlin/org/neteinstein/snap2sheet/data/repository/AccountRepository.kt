package org.neteinstein.snap2sheet.data.repository

import kotlinx.coroutines.flow.StateFlow
import org.neteinstein.snap2sheet.domain.model.GoogleAccount

/**
 * The signed-in Google account. Backed by an in-memory mock today — wiring this to real Google
 * Sign-In / OAuth (needed before Sheets access actually works) is tracked separately since it
 * requires a registered OAuth client per platform.
 */
interface AccountRepository {
    val account: StateFlow<GoogleAccount?>
    fun signIn()
    fun signOut()
}

class MockAccountRepository : AccountRepository {
    private val _account = kotlinx.coroutines.flow.MutableStateFlow<GoogleAccount?>(null)
    override val account: StateFlow<GoogleAccount?> = _account

    override fun signIn() {
        _account.value = GoogleAccount(email = "pedro.almeida@gmail.com", initials = "PA")
    }

    override fun signOut() {
        _account.value = null
    }
}
