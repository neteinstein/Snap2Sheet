package org.neteinstein.snap2sheet.data.local

import android.content.Context

/**
 * Set once from `Snap2SheetApplication.onCreate()`, before Koin starts, so [platformKeyValueStore]
 * can reach a [Context] without threading Android types through commonMain/Koin's shared module.
 */
object AndroidAppContext {
    lateinit var instance: Context
}
