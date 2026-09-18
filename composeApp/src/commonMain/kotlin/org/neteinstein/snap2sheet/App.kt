package org.neteinstein.snap2sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import org.neteinstein.snap2sheet.data.repository.SettingsRepository
import org.neteinstein.snap2sheet.ui.navigation.AppNavigation
import org.neteinstein.snap2sheet.ui.theme.FaturaTheme

@Composable
fun App(settingsRepository: SettingsRepository = koinInject()) {
    val theme by settingsRepository.theme.collectAsStateWithLifecycle()
    FaturaTheme(theme = theme) {
        AppNavigation()
    }
}
