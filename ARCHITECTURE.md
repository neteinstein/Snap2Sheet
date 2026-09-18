# Snap2Sheet Architecture

Same pattern as [neteinstein/loopgain](https://github.com/neteinstein/loopgain): Kotlin
Multiplatform + Compose Multiplatform, sharing the domain, data and UI layers between Android
and iOS from a single `composeApp` module.

## Layers

```
ui/          Compose screens, view models, navigation, theme
domain/      Pure Kotlin models — no Compose, no platform dependency
data/        Repositories (interface + in-memory mock impl) and the KeyValueStore expect/actual
di/          Koin module wiring repositories to view models
```

## Module structure

- `composeApp` — the shared KMP library (`commonMain`, `androidMain`, `iosMain`, `commonTest`)
- `androidApp` — thin Android application shell (`Application`, `MainActivity`, manifest, launcher icon)
- `iosApp` — SwiftUI wrapper (`iOSApp.swift`, `ContentView.swift`) plus the Xcode project

## State management

MVVM with `StateFlow`: each screen with meaningful state has a `ViewModel` (Koin `viewModel {}`)
exposing a single `StateFlow<UiState>` built with `combine(...).stateIn(...)`. Screens that are
pure display/navigation (Welcome, Scan) skip the ViewModel and take callbacks directly.

## Data layer

Every repository is declared as an interface (`InvoiceRepository`, `SpreadsheetRepository`,
`AccountRepository`, `SettingsRepository`) with an in-memory `Mock*` implementation registered in
`di/AppModule.kt`. `SettingsRepository` is the one exception — it's real, backed by
[`KeyValueStore`](composeApp/src/commonMain/kotlin/org/neteinstein/snap2sheet/data/local/KeyValueStore.kt),
an `expect`/`actual` wrapper around `SharedPreferences` (Android) / `NSUserDefaults` (iOS), same
as loopgain's.

The other three repositories are mocked because they depend on integrations this codebase
doesn't have credentials for yet:

- **AccountRepository** — needs a registered Google OAuth client (Android: Credential Manager /
  Google Identity Services; iOS: Google Sign-In SDK).
- **SpreadsheetRepository** / **InvoiceRepository**'s save path — needs the Google Sheets API
  (`https://www.googleapis.com/auth/spreadsheets`) called with that OAuth token.

Swapping a mock for a real implementation is scoped to one file each; the screens and view models
that consume the interfaces don't change.

## Navigation

One `NavHost` (`ui/navigation/AppNavigation.kt`) with a `Screen` sealed class, mirroring the
Fatura design's eight screens: Welcome → SignIn → Home → {Scan → Review → Destination, History,
Settings}.

## Theme

`ui/theme/Color.kt` and `Theme.kt` hold the design's brand tokens (accent blue `#2E5AAC`, the
neutral ground, status colors) as a Material 3 `ColorScheme`. The design's three Google Fonts
(Space Grotesk, Manrope, Space Mono) aren't bundled as font files yet — see the doc comment on
`FaturaFonts` for how to add them.
