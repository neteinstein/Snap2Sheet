package org.neteinstein.snap2sheet.data.local

import kotlinx.browser.localStorage

private class WasmKeyValueStore : KeyValueStore {
    override fun getString(key: String): String? = localStorage.getItem(key)

    override fun putString(key: String, value: String) {
        localStorage.setItem(key, value)
    }
}

actual fun platformKeyValueStore(): KeyValueStore = WasmKeyValueStore()
