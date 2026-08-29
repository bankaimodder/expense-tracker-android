# ExpenseTracker

A native Android app for tracking personal income and expenses, built with
Kotlin and Jetpack Compose as a portfolio project.

## Screenshots

See [`screenshots/`](screenshots/) for a list of the screens to capture.
Once added, they'll show up here:

| Dashboard | Add Transaction | History |
|---|---|---|
| ![Dashboard](screenshots/dashboard.png) | ![Add Transaction](screenshots/add_transaction.png) | ![History](screenshots/history.png) |

| Statistics | Settings |
|---|---|
| ![Statistics](screenshots/statistics.png) | ![Settings](screenshots/settings.png) |

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
- Real persistence with **Room** — nothing is mocked or in-memory; every
  transaction survives app restarts.
- Explicit **loading, empty and error states** on every screen instead of
  blank screens or silent failures.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM + Clean Architecture |
| Persistence | Room (transactions), DataStore Preferences (settings) |
| DI | Hilt |
| Navigation | Navigation Compose |
| Async | Kotlin Coroutines & Flow |
| Testing | JUnit 4, kotlinx-coroutines-test |

## Architecture

The codebase is split into `domain`, `data`, and `presentation` layers, with
dependencies pointing inward toward `domain`. See
[`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for the full breakdown,
including how each screen's ViewModel, UI state, and use cases fit together.

```
app/src/main/java/com/expensetracker/app/
├── data/                  # Room, DataStore, repository implementations
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   ├── preferences/
│   └── repository/
├── di/                    # Hilt modules
├── domain/                # Models, repository interfaces, use cases
│   ├── model/
│   ├── repository/
│   └── usecase/
├── presentation/          # Compose screens, ViewModels, navigation, theme
│   ├── addtransaction/
│   ├── common/
│   ├── dashboard/
│   ├── history/
│   ├── main/
│   ├── navigation/
│   ├── settings/
│   ├── statistics/
│   └── theme/
├── ExpenseTrackerApplication.kt
└── MainActivity.kt
```

## Getting started

See [`docs/INSTALLATION.md`](docs/INSTALLATION.md) for full setup
instructions. The short version:

```bash
git clone https://github.com/bankaimodder/expense-tracker-android.git
cd expense-tracker-android
./gradlew installDebug
```

Requires Android Studio, JDK 17, and a device/emulator running Android 8.0
(API 26) or later. No backend, API keys, or `google-services.json` needed —
everything runs and persists locally.

## Running tests

```bash
./gradlew testDebugUnitTest
```

Unit tests cover the domain layer, including transaction validation
(`AddTransactionUseCase`) and dashboard aggregation logic
(`GetDashboardDataUseCase`), using a fake in-memory repository so they run
fast on the JVM without a device.

## Project status

This is a solo learning/portfolio project. Planned improvements:

- Recurring transactions
- Budgets per category with alerts
- CSV export
- Home screen widget

## License

This project is available for personal and educational use.
