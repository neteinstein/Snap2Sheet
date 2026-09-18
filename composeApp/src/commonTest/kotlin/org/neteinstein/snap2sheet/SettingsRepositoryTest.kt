package org.neteinstein.snap2sheet

import org.neteinstein.snap2sheet.data.local.KeyValueStore
import org.neteinstein.snap2sheet.data.repository.DefaultSettingsRepository
import org.neteinstein.snap2sheet.domain.model.AppTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

private class InMemoryKeyValueStore : KeyValueStore {
    private val values = mutableMapOf<String, String>()
    override fun getString(key: String): String? = values[key]
    override fun putString(key: String, value: String) {
        values[key] = value
    }
}

class SettingsRepositoryTest {

    @Test
    fun setTheme_persistsAcrossRepositoryInstances() {
        val store = InMemoryKeyValueStore()
        val repository = DefaultSettingsRepository(store)

        repository.setTheme(AppTheme.DARK)

        val reloaded = DefaultSettingsRepository(store)
        assertEquals(AppTheme.DARK, reloaded.theme.value)
    }

    @Test
    fun setAppendRules_persistsIndividualFlags() {
        val store = InMemoryKeyValueStore()
        val repository = DefaultSettingsRepository(store)

        repository.setAppendRules(repository.appendRules.value.copy(newSheetTabEachMonth = true))

        val reloaded = DefaultSettingsRepository(store)
        assertEquals(true, reloaded.appendRules.value.newSheetTabEachMonth)
        assertEquals(true, reloaded.appendRules.value.matchColumnsByHeader)
    }

    @Test
    fun defaultNotifyOnScanFailure_isEnabled() {
        val repository = DefaultSettingsRepository(InMemoryKeyValueStore())

        assertEquals(true, repository.notifyOnScanFailure.value)
        assertFalse(repository.appendRules.value.newSheetTabEachMonth)
    }
}
