package org.neteinstein.snap2sheet.di

import org.koin.core.context.startKoin

/**
 * Starts Koin with [appModule]. Called from `Snap2SheetApplication.onCreate()` on Android and
 * from `iosApp/iosApp/iOSApp.swift`'s `init()` (as `InitKoinKt.doInitKoin()`) on iOS.
 * Kotlin/Native's Objective-C exporter renames it: an `init`-prefixed top-level function would
 * otherwise collide with Objective-C's `init` initializer convention, so it's exported as
 * `doInitKoin()`.
 */
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
