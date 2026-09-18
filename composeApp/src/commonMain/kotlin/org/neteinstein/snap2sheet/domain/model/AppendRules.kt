package org.neteinstein.snap2sheet.domain.model

/** How a saved invoice is appended to the destination spreadsheet. */
data class AppendRules(
    val matchColumnsByHeader: Boolean = true,
    val skipDuplicateInvoices: Boolean = true,
    val newSheetTabEachMonth: Boolean = false,
)
