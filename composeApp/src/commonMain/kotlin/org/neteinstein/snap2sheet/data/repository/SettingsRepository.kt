package org.neteinstein.snap2sheet.data.repository

import kotlinx.coroutines.flow.StateFlow
import org.neteinstein.snap2sheet.domain.model.AppTheme
import org.neteinstein.snap2sheet.domain.model.AppendRules

/** User-configurable preferences, persisted across launches via [org.neteinstein.snap2sheet.data.local.KeyValueStore]. */
interface SettingsRepository {
    val theme: StateFlow<AppTheme>
    fun setTheme(theme: AppTheme)

    val appendRules: StateFlow<AppendRules>
    fun setAppendRules(rules: AppendRules)

    /** "Always save here automatically", offered on the Destination screen. */
    val alwaysSaveToDefaultSpreadsheet: StateFlow<Boolean>
    fun setAlwaysSaveToDefaultSpreadsheet(enabled: Boolean)

    val notifyOnScanFailure: StateFlow<Boolean>
    fun setNotifyOnScanFailure(enabled: Boolean)
}
