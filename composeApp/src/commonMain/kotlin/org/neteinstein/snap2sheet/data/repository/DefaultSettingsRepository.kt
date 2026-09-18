package org.neteinstein.snap2sheet.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.neteinstein.snap2sheet.data.local.KeyValueStore
import org.neteinstein.snap2sheet.domain.model.AppTheme
import org.neteinstein.snap2sheet.domain.model.AppendRules

private const val KEY_THEME = "theme"
private const val KEY_MATCH_BY_HEADER = "append_match_by_header"
private const val KEY_SKIP_DUPLICATES = "append_skip_duplicates"
private const val KEY_NEW_TAB_MONTHLY = "append_new_tab_monthly"
private const val KEY_ALWAYS_SAVE_DEFAULT = "always_save_default"
private const val KEY_NOTIFY_ON_FAILURE = "notify_on_failure"

class DefaultSettingsRepository(private val store: KeyValueStore) : SettingsRepository {

    private val _theme = MutableStateFlow(
        store.getString(KEY_THEME)?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() } ?: AppTheme.SYSTEM
    )
    override val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    override fun setTheme(theme: AppTheme) {
        _theme.value = theme
        store.putString(KEY_THEME, theme.name)
    }

    private val _appendRules = MutableStateFlow(
        AppendRules(
            matchColumnsByHeader = store.getString(KEY_MATCH_BY_HEADER)?.toBooleanStrictOrNull() ?: true,
            skipDuplicateInvoices = store.getString(KEY_SKIP_DUPLICATES)?.toBooleanStrictOrNull() ?: true,
            newSheetTabEachMonth = store.getString(KEY_NEW_TAB_MONTHLY)?.toBooleanStrictOrNull() ?: false,
        )
    )
    override val appendRules: StateFlow<AppendRules> = _appendRules.asStateFlow()

    override fun setAppendRules(rules: AppendRules) {
        _appendRules.value = rules
        store.putString(KEY_MATCH_BY_HEADER, rules.matchColumnsByHeader.toString())
        store.putString(KEY_SKIP_DUPLICATES, rules.skipDuplicateInvoices.toString())
        store.putString(KEY_NEW_TAB_MONTHLY, rules.newSheetTabEachMonth.toString())
    }

    private val _alwaysSaveToDefaultSpreadsheet = MutableStateFlow(
        store.getString(KEY_ALWAYS_SAVE_DEFAULT)?.toBooleanStrictOrNull() ?: true
    )
    override val alwaysSaveToDefaultSpreadsheet: StateFlow<Boolean> = _alwaysSaveToDefaultSpreadsheet.asStateFlow()

    override fun setAlwaysSaveToDefaultSpreadsheet(enabled: Boolean) {
        _alwaysSaveToDefaultSpreadsheet.value = enabled
        store.putString(KEY_ALWAYS_SAVE_DEFAULT, enabled.toString())
    }

    private val _notifyOnScanFailure = MutableStateFlow(
        store.getString(KEY_NOTIFY_ON_FAILURE)?.toBooleanStrictOrNull() ?: true
    )
    override val notifyOnScanFailure: StateFlow<Boolean> = _notifyOnScanFailure.asStateFlow()

    override fun setNotifyOnScanFailure(enabled: Boolean) {
        _notifyOnScanFailure.value = enabled
        store.putString(KEY_NOTIFY_ON_FAILURE, enabled.toString())
    }
}
