package org.neteinstein.snap2sheet.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.neteinstein.snap2sheet.domain.model.SpreadsheetDestination

/**
 * The user's Google Sheets spreadsheets available as invoice destinations. Backed by an
 * in-memory mock today — listing/creating real spreadsheets needs the Google Sheets API wired
 * up behind [AccountRepository]'s sign-in.
 */
interface SpreadsheetRepository {
    val destinations: StateFlow<List<SpreadsheetDestination>>
    val selectedDestinationId: StateFlow<String?>
    fun selectDestination(id: String)
    fun createDestination(name: String): SpreadsheetDestination
}

class MockSpreadsheetRepository : SpreadsheetRepository {
    private val _destinations = MutableStateFlow(
        listOf(
            SpreadsheetDestination(id = "sheet-despesas-2026", name = "Despesas 2026", lastEditedLabel = "Edited today"),
            SpreadsheetDestination(id = "sheet-contabilidade-pt", name = "Contabilidade PT", lastEditedLabel = "Edited 3 days ago"),
            SpreadsheetDestination(id = "sheet-fatura-startup", name = "Fatura Startup Co", lastEditedLabel = "Edited last week"),
        )
    )
    override val destinations: StateFlow<List<SpreadsheetDestination>> = _destinations.asStateFlow()

    private val _selectedDestinationId = MutableStateFlow<String?>(_destinations.value.first().id)
    override val selectedDestinationId: StateFlow<String?> = _selectedDestinationId.asStateFlow()

    override fun selectDestination(id: String) {
        _selectedDestinationId.value = id
    }

    override fun createDestination(name: String): SpreadsheetDestination {
        val destination = SpreadsheetDestination(
            id = "sheet-${_destinations.value.size + 1}-${name.lowercase().replace(" ", "-")}",
            name = name,
            lastEditedLabel = "Just created",
        )
        _destinations.value = _destinations.value + destination
        _selectedDestinationId.value = destination.id
        return destination
    }
}
