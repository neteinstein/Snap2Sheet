package org.neteinstein.snap2sheet.domain.model

/** The Google account Fatura saves invoices on behalf of. */
data class GoogleAccount(
    val email: String,
    val initials: String,
)
