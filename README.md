# Snap2Sheet

An app to snap a picture of an invoice and place it on Google Sheets.

The product name shown in the app itself is **Fatura** — scan an invoice's QR code and it's
parsed and appended as a row to a Google Sheet you choose, no typing required.

## Stack

Kotlin Multiplatform + Compose Multiplatform, targeting Android and iOS from one shared UI 
See [ARCHITECTURE.md](ARCHITECTURE.md) for the layer breakdown.

- Kotlin Multiplatform / Compose Multiplatform
- Koin for dependency injection
- Coroutines + `StateFlow` for state
- Ktor (client) for networking, once the Google Sheets API is wired up
- Coil for image loading

## Project layout

```
composeApp/          Shared KMP module
  src/commonMain/     domain/, data/, ui/ (screens, components, theme, navigation), di/
  src/androidMain/    Android actuals (KeyValueStore, app Context)
  src/iosMain/        iOS actuals (KeyValueStore, MainViewController)
androidApp/           Android application entry point
iosApp/               iOS application wrapper (SwiftUI + Xcode project)
```

## Screens

Welcome → Connect Google → Home → Scan → Review → Choose Spreadsheet, plus History and Settings —
implemented from the Fatura design (`Main`, `SignIn`, `Home`, `Scan`, `Review`, `Destination`,
`History`, `Settings`).

## Status

The Google Sign-In and Google Sheets integrations are stubbed with in-memory mock repositories
(`data/repository/Mock*Repository.kt`) so the full UI and navigation flow work end-to-end today.
Wiring up real Google OAuth and the Sheets API is tracked separately — see the doc comments on
`AccountRepository`, `SpreadsheetRepository` and `InvoiceRepository`.

## Building

```bash
./gradlew :androidApp:assembleDebug
```

iOS: open `iosApp/iosApp.xcodeproj` in Xcode, or build the `composeApp` framework via Gradle
first (`./gradlew :composeApp:embedAndSignAppleFrameworkForXcode` from Xcode's build phase, as
usual for a KMP project).
