package org.neteinstein.snap2sheet.domain.model

/** A Google Sheets spreadsheet the user can append scanned invoices to. */
data class SpreadsheetDestination(
    val id: String,
    val name: String,
    val lastEditedLabel: String,
)
