package org.neteinstein.snap2sheet.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.neteinstein.snap2sheet.data.repository.AccountRepository
import org.neteinstein.snap2sheet.data.repository.SettingsRepository
import org.neteinstein.snap2sheet.data.repository.SpreadsheetRepository
import org.neteinstein.snap2sheet.domain.model.AppendRules
import org.neteinstein.snap2sheet.domain.model.GoogleAccount

data class SettingsUiState(
    val account: GoogleAccount? = null,
    val defaultSpreadsheetName: String? = null,
    val appendRules: AppendRules = AppendRules(),
    val notifyOnScanFailure: Boolean = true,
)

class SettingsViewModel(
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository,
    spreadsheetRepository: SpreadsheetRepository,
) : ViewModel() {

    val state: StateFlow<SettingsUiState> = combine(
        accountRepository.account,
        spreadsheetRepository.destinations,
        spreadsheetRepository.selectedDestinationId,
        settingsRepository.appendRules,
        settingsRepository.notifyOnScanFailure,
    ) { account, destinations, selectedId, rules, notify ->
        SettingsUiState(
            account = account,
            defaultSpreadsheetName = destinations.firstOrNull { it.id == selectedId }?.name,
            appendRules = rules,
            notifyOnScanFailure = notify,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun signOut() = accountRepository.signOut()

    fun setMatchColumnsByHeader(value: Boolean) {
        settingsRepository.setAppendRules(settingsRepository.appendRules.value.copy(matchColumnsByHeader = value))
    }

    fun setSkipDuplicates(value: Boolean) {
        settingsRepository.setAppendRules(settingsRepository.appendRules.value.copy(skipDuplicateInvoices = value))
    }

    fun setNewSheetTabEachMonth(value: Boolean) {
        settingsRepository.setAppendRules(settingsRepository.appendRules.value.copy(newSheetTabEachMonth = value))
    }

    fun setNotifyOnScanFailure(value: Boolean) = settingsRepository.setNotifyOnScanFailure(value)
}
