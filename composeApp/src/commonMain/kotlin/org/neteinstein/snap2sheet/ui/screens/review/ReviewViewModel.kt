package org.neteinstein.snap2sheet.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.neteinstein.snap2sheet.data.repository.InvoiceRepository
import org.neteinstein.snap2sheet.data.repository.SpreadsheetRepository
import org.neteinstein.snap2sheet.domain.model.Invoice

data class ReviewUiState(
    val invoice: Invoice? = null,
    val selectedSpreadsheetName: String? = null,
)

class ReviewViewModel(
    private val invoiceRepository: InvoiceRepository,
    spreadsheetRepository: SpreadsheetRepository,
) : ViewModel() {

    init {
        if (invoiceRepository.draftInvoice.value == null) {
            invoiceRepository.startDraftFromScan()
        }
    }

    val state: StateFlow<ReviewUiState> = combine(
        invoiceRepository.draftInvoice,
        spreadsheetRepository.destinations,
        spreadsheetRepository.selectedDestinationId,
    ) { draft, destinations, selectedId ->
        ReviewUiState(
            invoice = draft,
            selectedSpreadsheetName = destinations.firstOrNull { it.id == selectedId }?.name,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReviewUiState())

    fun updateNifEmitente(value: String) = updateDraft { it.copy(nifEmitente = value) }
    fun updateNifAdquirente(value: String) = updateDraft { it.copy(nifAdquirente = value) }
    fun updateDocumentType(value: String) = updateDraft { it.copy(documentType = value) }
    fun updateDocumentNumber(value: String) = updateDraft { it.copy(documentNumber = value) }
    fun updateDate(value: String) = updateDraft { it.copy(date = value) }
    fun updateAtcud(value: String) = updateDraft { it.copy(atcud = value) }

    fun updateTaxBase(value: String) = updateDraft { it.copy(taxBase = value.toEuroOrElse(it.taxBase)) }
    fun updateVat(value: String) = updateDraft { it.copy(vat = value.toEuroOrElse(it.vat)) }
    fun updateTotal(value: String) = updateDraft { it.copy(total = value.toEuroOrElse(it.total)) }

    private fun updateDraft(transform: (Invoice) -> Invoice) {
        val current = invoiceRepository.draftInvoice.value ?: return
        invoiceRepository.updateDraft(transform(current))
    }

    private fun String.toEuroOrElse(fallback: Double): Double =
        this.replace("€", "").replace(",", ".").trim().toDoubleOrNull() ?: fallback
}
