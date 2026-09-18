package org.neteinstein.snap2sheet.domain.model

/**
 * A single invoice scanned from a Portuguese fatura QR code (the ATCUD / AT Portugal scheme:
 * NIF emitente/adquirente, ATCUD, tax base and IVA are the fields that scheme's QR payload
 * encodes) and its state on the way to a Google Sheets row.
 */
data class Invoice(
    val id: String,
    val merchantName: String,
    val documentType: String,
    val documentNumber: String,
    val date: String,
    val atcud: String,
    val nifEmitente: String,
    val nifAdquirente: String,
    val taxBase: Double,
    val vat: Double,
    val total: Double,
    val status: InvoiceStatus,
    val scannedAtLabel: String,
    val destinationSpreadsheetName: String?,
)
