package org.neteinstein.snap2sheet.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.neteinstein.snap2sheet.domain.model.Invoice
import org.neteinstein.snap2sheet.domain.model.InvoiceStatus

/**
 * Scanned invoices, most recent first. Backed by an in-memory mock today — persisting and
 * actually appending rows to Google Sheets needs the Sheets API wired up behind
 * [AccountRepository]'s sign-in.
 */
interface InvoiceRepository {
    val invoices: StateFlow<List<Invoice>>

    /** The invoice currently being reviewed after a scan, before it's saved to a spreadsheet. */
    val draftInvoice: StateFlow<Invoice?>

    fun addInvoice(invoice: Invoice)
    fun lastScanned(): Invoice?

    /** Decodes the (simulated) QR payload just scanned into a new [draftInvoice]. */
    fun startDraftFromScan(): Invoice

    fun updateDraft(invoice: Invoice)

    /** Moves [draftInvoice] into [invoices] as [InvoiceStatus.SYNCED] and clears the draft. */
    fun commitDraft(destinationSpreadsheetName: String): Invoice?
}

class MockInvoiceRepository : InvoiceRepository {
    private val _invoices = MutableStateFlow(
        listOf(
            Invoice(
                id = "inv-1",
                merchantName = "Continente",
                documentType = "Fatura-Recibo (FR)",
                documentNumber = "FS 2026/004821",
                date = "18/09/2026",
                atcud = "AAJFJMM9-4821",
                nifEmitente = "500 100 200",
                nifAdquirente = "999 999 990",
                taxBase = 14.98,
                vat = 3.44,
                total = 18.42,
                status = InvoiceStatus.SYNCED,
                scannedAtLabel = "Today · 14:32",
                destinationSpreadsheetName = "Despesas 2026",
            ),
            Invoice(
                id = "inv-2",
                merchantName = "Worten",
                documentType = "Fatura-Recibo (FR)",
                documentNumber = "FS 2026/018832",
                date = "17/09/2026",
                atcud = "AAJFJMM9-1883",
                nifEmitente = "502 233 445",
                nifAdquirente = "999 999 990",
                taxBase = 105.69,
                vat = 24.30,
                total = 129.99,
                status = InvoiceStatus.SYNCED,
                scannedAtLabel = "Yesterday · 18:05",
                destinationSpreadsheetName = "Despesas 2026",
            ),
            Invoice(
                id = "inv-3",
                merchantName = "CTT",
                documentType = "Fatura (FT)",
                documentNumber = "FT 2026/002211",
                date = "17/09/2026",
                atcud = "AAJFJMM9-2211",
                nifEmitente = "500 830 024",
                nifAdquirente = "999 999 990",
                taxBase = 5.28,
                vat = 1.22,
                total = 6.50,
                status = InvoiceStatus.NEEDS_REVIEW,
                scannedAtLabel = "Yesterday · 09:14",
                destinationSpreadsheetName = "Despesas 2026",
            ),
            Invoice(
                id = "inv-4",
                merchantName = "Galp",
                documentType = "Fatura-Recibo (FR)",
                documentNumber = "FS 2026/077310",
                date = "14/09/2026",
                atcud = "AAJFJMM9-7731",
                nifEmitente = "500 011 016",
                nifAdquirente = "999 999 990",
                taxBase = 42.36,
                vat = 9.74,
                total = 52.10,
                status = InvoiceStatus.SYNCED,
                scannedAtLabel = "Mon · Despesas 2026",
                destinationSpreadsheetName = "Despesas 2026",
            ),
            Invoice(
                id = "inv-5",
                merchantName = "Pingo Doce",
                documentType = "Fatura-Recibo (FR)",
                documentNumber = "FS 2026/055120",
                date = "14/09/2026",
                atcud = "AAJFJMM9-5512",
                nifEmitente = "501 532 089",
                nifAdquirente = "999 999 990",
                taxBase = 28.35,
                vat = 6.52,
                total = 34.87,
                status = InvoiceStatus.SYNCED,
                scannedAtLabel = "Mon · Contabilidade PT",
                destinationSpreadsheetName = "Contabilidade PT",
            ),
            Invoice(
                id = "inv-6",
                merchantName = "Fnac",
                documentType = "Fatura-Recibo (FR)",
                documentNumber = "FS 2026/009943",
                date = "13/09/2026",
                atcud = "AAJFJMM9-0994",
                nifEmitente = "503 233 987",
                nifAdquirente = "999 999 990",
                taxBase = 72.36,
                vat = 16.64,
                total = 89.00,
                status = InvoiceStatus.FAILED,
                scannedAtLabel = "Sun · Despesas 2026",
                destinationSpreadsheetName = "Despesas 2026",
            ),
        )
    )
    override val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    private val _draftInvoice = MutableStateFlow<Invoice?>(null)
    override val draftInvoice: StateFlow<Invoice?> = _draftInvoice.asStateFlow()

    override fun addInvoice(invoice: Invoice) {
        _invoices.value = listOf(invoice) + _invoices.value
    }

    override fun lastScanned(): Invoice? = _invoices.value.firstOrNull()

    override fun startDraftFromScan(): Invoice {
        // A real QR decode reads the AT Portugal ATCUD payload off the camera frame; this mock
        // stands in for that until the camera pipeline is wired up (see ScanScreen).
        val draft = Invoice(
            id = "inv-${_invoices.value.size + _draftCounter++}",
            merchantName = "Continente",
            documentType = "Fatura-Recibo (FR)",
            documentNumber = "FS 2026/004821",
            date = "18/09/2026",
            atcud = "AAJFJMM9-4821",
            nifEmitente = "500 100 200",
            nifAdquirente = "999 999 990",
            taxBase = 14.98,
            vat = 3.44,
            total = 18.42,
            status = InvoiceStatus.NEEDS_REVIEW,
            scannedAtLabel = "Just now",
            destinationSpreadsheetName = null,
        )
        _draftInvoice.value = draft
        return draft
    }

    override fun updateDraft(invoice: Invoice) {
        _draftInvoice.value = invoice
    }

    override fun commitDraft(destinationSpreadsheetName: String): Invoice? {
        val draft = _draftInvoice.value ?: return null
        val saved = draft.copy(status = InvoiceStatus.SYNCED, destinationSpreadsheetName = destinationSpreadsheetName)
        addInvoice(saved)
        _draftInvoice.value = null
        return saved
    }

    private var _draftCounter = 100
}
