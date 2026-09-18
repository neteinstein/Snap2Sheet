package org.neteinstein.snap2sheet.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.neteinstein.snap2sheet.data.repository.AccountRepository
import org.neteinstein.snap2sheet.data.repository.InvoiceRepository
import org.neteinstein.snap2sheet.domain.model.GoogleAccount
import org.neteinstein.snap2sheet.domain.model.Invoice

data class HomeUiState(
    val account: GoogleAccount? = null,
    val recentInvoices: List<Invoice> = emptyList(),
    val invoicesThisMonth: Int = 0,
    val totalThisMonth: Double = 0.0,
    val primarySpreadsheetName: String? = null,
)

class HomeViewModel(
    invoiceRepository: InvoiceRepository,
    accountRepository: AccountRepository,
) : ViewModel() {

    val state: StateFlow<HomeUiState> = combine(
        invoiceRepository.invoices,
        accountRepository.account,
    ) { invoices, account ->
        HomeUiState(
            account = account,
            recentInvoices = invoices.take(3),
            invoicesThisMonth = invoices.size,
            totalThisMonth = invoices.sumOf { it.total },
            primarySpreadsheetName = invoices.firstOrNull()?.destinationSpreadsheetName,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}
