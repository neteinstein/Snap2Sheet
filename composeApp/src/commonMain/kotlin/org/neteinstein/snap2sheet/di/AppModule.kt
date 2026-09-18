package org.neteinstein.snap2sheet.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.neteinstein.snap2sheet.data.local.platformKeyValueStore
import org.neteinstein.snap2sheet.data.repository.AccountRepository
import org.neteinstein.snap2sheet.data.repository.DefaultAccountRepository
import org.neteinstein.snap2sheet.data.repository.DefaultSettingsRepository
import org.neteinstein.snap2sheet.data.repository.InvoiceRepository
import org.neteinstein.snap2sheet.data.repository.MockInvoiceRepository
import org.neteinstein.snap2sheet.data.repository.MockSpreadsheetRepository
import org.neteinstein.snap2sheet.data.repository.SettingsRepository
import org.neteinstein.snap2sheet.data.repository.SpreadsheetRepository
import org.neteinstein.snap2sheet.ui.screens.destination.DestinationViewModel
import org.neteinstein.snap2sheet.ui.screens.history.HistoryViewModel
import org.neteinstein.snap2sheet.ui.screens.home.HomeViewModel
import org.neteinstein.snap2sheet.ui.screens.review.ReviewViewModel
import org.neteinstein.snap2sheet.ui.screens.settings.SettingsViewModel

internal val appModule = module {
    single { platformKeyValueStore() }
    single { DefaultSettingsRepository(get()) } bind SettingsRepository::class
    single { DefaultAccountRepository() } bind AccountRepository::class
    single { MockInvoiceRepository() } bind InvoiceRepository::class
    single { MockSpreadsheetRepository() } bind SpreadsheetRepository::class

    viewModel { HomeViewModel(get(), get()) }
    viewModel { ReviewViewModel(get(), get()) }
    viewModel { DestinationViewModel(get(), get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
}
