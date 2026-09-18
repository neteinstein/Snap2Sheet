package org.neteinstein.snap2sheet.ui.screens.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.neteinstein.snap2sheet.data.repository.InvoiceRepository
import org.neteinstein.snap2sheet.data.repository.SpreadsheetRepository
import org.neteinstein.snap2sheet.domain.model.Invoice
import org.neteinstein.snap2sheet.domain.model.SpreadsheetDestination

data class DestinationUiState(
    val destinations: List<SpreadsheetDestination> = emptyList(),
    val selectedId: String? = null,
    val query: String = "",
    val alwaysSaveAutomatically: Boolean = true,
) {
    val filtered: List<SpreadsheetDestination>
        get() = if (query.isBlank()) destinations else destinations.filter { it.name.contains(query, ignoreCase = true) }
}

class DestinationViewModel(
    private val spreadsheetRepository: SpreadsheetRepository,
    private val invoiceRepository: InvoiceRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val alwaysSave = MutableStateFlow(true)

    val state: StateFlow<DestinationUiState> = combine(
        spreadsheetRepository.destinations,
        spreadsheetRepository.selectedDestinationId,
        query,
        alwaysSave,
    ) { destinations, selectedId, q, always ->
        DestinationUiState(destinations = destinations, selectedId = selectedId, query = q, alwaysSaveAutomatically = always)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DestinationUiState())

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onAlwaysSaveChange(value: Boolean) {
        alwaysSave.value = value
    }

    fun selectDestination(id: String) {
        spreadsheetRepository.selectDestination(id)
    }

    fun createDestination(name: String) {
        spreadsheetRepository.createDestination(name)
    }

    /** Commits the draft invoice under review to the currently selected spreadsheet. */
    fun saveDraftToSelectedDestination(): Invoice? {
        val name = spreadsheetRepository.destinations.value
            .firstOrNull { it.id == spreadsheetRepository.selectedDestinationId.value }
            ?.name ?: return null
        return invoiceRepository.commitDraft(name)
    }
}
