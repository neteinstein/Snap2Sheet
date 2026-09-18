package org.neteinstein.snap2sheet.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.neteinstein.snap2sheet.data.repository.InvoiceRepository
import org.neteinstein.snap2sheet.domain.model.Invoice
import org.neteinstein.snap2sheet.domain.model.InvoiceStatus

enum class HistoryFilter(val label: String) {
    ALL("All"), SYNCED("Synced"), NEEDS_REVIEW("Needs review"), FAILED("Failed")
}

data class HistoryUiState(
    val filter: HistoryFilter = HistoryFilter.ALL,
    val invoices: List<Invoice> = emptyList(),
)

class HistoryViewModel(invoiceRepository: InvoiceRepository) : ViewModel() {

    private val filter = MutableStateFlow(HistoryFilter.ALL)

    val state: StateFlow<HistoryUiState> = combine(invoiceRepository.invoices, filter) { invoices, f ->
        val filtered = when (f) {
            HistoryFilter.ALL -> invoices
            HistoryFilter.SYNCED -> invoices.filter { it.status == InvoiceStatus.SYNCED }
            HistoryFilter.NEEDS_REVIEW -> invoices.filter { it.status == InvoiceStatus.NEEDS_REVIEW }
            HistoryFilter.FAILED -> invoices.filter { it.status == InvoiceStatus.FAILED }
        }
        HistoryUiState(filter = f, invoices = filtered)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryUiState())

    fun setFilter(value: HistoryFilter) {
        filter.value = value
    }
}
