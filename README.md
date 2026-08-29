# ExpenseTracker

A native Android app for tracking personal income and expenses, built with
Kotlin and Jetpack Compose.

## Features

- **Dashboard** — total balance, this month's income and expenses, and the
  five most recent transactions at a glance.
- **Add transactions** — log income or expenses with an amount, category,
  optional note, and date (with a Material 3 date picker).
- **Categories** — Food, Transport, Shopping, Entertainment, Bills, Salary,
  Other, each with its own icon and color.
- **Statistics** — a category breakdown pie chart for the current month and
  a 6-month income vs. expense bar chart, both drawn with Compose `Canvas`
  (no charting library).
- **Transaction history** — search transactions by note, filter by category,
  and delete a transaction with a confirmation dialog.
- **Settings** — toggle dark mode and pick a currency (USD, EUR, GBP, INR,
  JPY); both are persisted with DataStore and applied across the whole app.
- Real persistence with **Room** — every transaction survives app restarts.
- Explicit loading, empty, and error states on every screen.

## Architecture

The codebase follows Clean Architecture, split into `domain`, `data`, and
`presentation` layers, with dependencies pointing inward toward `domain`.

```
app/src/main/java/com/expensetracker/app/
├── data/                  # Room, DataStore, repository implementations
├── di/                    # Hilt modules
├── domain/                # Models, repository interfaces, use cases
└── presentation/          # Compose screens, ViewModels, navigation, theme
```

Each screen follows MVVM: a `*UiState` data class, a `@HiltViewModel` that
exposes a single `StateFlow<UiState>` built from use cases, and a stateless
`@Composable` screen that renders that state and forwards user actions back
through plain callbacks. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
for the full breakdown.

## Installation

```bash
git clone https://github.com/bankaimodder/expense-tracker-android.git
cd expense-tracker-android
./gradlew installDebug
```

Requires Android Studio, JDK 17, and a device/emulator running Android 8.0
(API 26) or later. No backend, API keys, or `google-services.json` needed —
everything runs and persists locally. See
[`docs/INSTALLATION.md`](docs/INSTALLATION.md) for full setup instructions,
troubleshooting, and how to run the test suite.

## Technologies Used

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM + Clean Architecture |
| Persistence | Room (transactions), DataStore Preferences (settings) |
| Dependency Injection | Hilt |
| Navigation | Navigation Compose |
| Async | Kotlin Coroutines & Flow |
| Testing | JUnit 4, kotlinx-coroutines-test |
| CI | GitHub Actions |
