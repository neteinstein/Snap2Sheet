package org.neteinstein.snap2sheet.domain.model

/** Where a scanned invoice stands in the save-to-spreadsheet pipeline. */
enum class InvoiceStatus {
    SYNCED, NEEDS_REVIEW, FAILED
}
