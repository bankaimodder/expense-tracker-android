# Architecture

ExpenseTracker follows **Clean Architecture** split into three layers, combined
with the standard Android **MVVM** pattern on top of the presentation layer.
The goal is to keep business logic independent of Android framework classes
and easy to unit test.

```
presentation/  -->  domain/  <--  data/
  (Compose UI,       (models,      (Room, DataStore,
   ViewModels)        use cases,    repository
                       repository   implementations)
                       interfaces)
```

Dependencies always point **inward**: `presentation` depends on `domain`,
`data` depends on `domain`, but `domain` depends on nothing else in the app.
This is what lets the domain layer stay a plain Kotlin module with no Android
imports.

## Layers

### 1. Domain layer (`domain/`)

The core of the app. Contains:

- **Models** – plain Kotlin data classes (`Transaction`, `Category`,
  `Currency`, `DashboardData`, `Statistics`, ...). No Room or Compose
  annotations here. `Category` is a good example: the domain enum only
  knows its display name, and the icon/color used to render it live in
  `presentation/common/CategoryPresentation.kt` as extension properties,
  so Jetpack Compose types never leak into the domain layer.
- **Repository interfaces** – `TransactionRepository`, `SettingsRepository`.
  The domain layer only knows about these contracts, never about Room or
  DataStore.
- **Use cases** – one class per business operation
  (`AddTransactionUseCase`, `DeleteTransactionUseCase`,
  `GetTransactionsUseCase`, `GetDashboardDataUseCase`,
  `GetStatisticsUseCase`, the settings use cases). Each use case is a small
  `operator fun invoke(...)` class injected with `@Inject constructor`, which
  keeps ViewModels thin and makes each rule independently testable.

  `AddTransactionUseCase` is a good example of why this layer exists: the
  validation rule ("amount must be greater than zero") lives in exactly one
  place instead of being duplicated in the ViewModel and repeated for every
  screen that can create a transaction.

### 2. Data layer (`data/`)

Implements the domain repository interfaces using real persistence:

- **Room** (`data/local/`) – `TransactionEntity`, `TransactionDao` and
  `ExpenseDatabase` store transactions on disk. Mapping between the
  persistence model (`TransactionEntity`) and the domain model
  (`Transaction`) happens through small `toDomain()` / `toEntity()`
  extension functions, so Room annotations never leak into the domain layer.
- **DataStore** (`data/preferences/`) – `SettingsDataStore` backs dark mode
  and currency preferences with Jetpack DataStore Preferences, wrapped by
  `SettingsRepositoryImpl`.
- **Repositories** (`data/repository/`) – `TransactionRepositoryImpl` and
  `SettingsRepositoryImpl` implement the domain interfaces and are the only
  classes that know both the domain model and the storage details. All
  suspend calls and flows are dispatched on `Dispatchers.IO`.

### 3. Presentation layer (`presentation/`)

Organized **by feature**, not by type, so everything for one screen lives
together:

```
presentation/
  dashboard/        DashboardScreen, DashboardViewModel, DashboardUiState
  addtransaction/    AddTransactionScreen, AddTransactionViewModel, ...
  history/           HistoryScreen, HistoryViewModel, ...
  statistics/        StatisticsScreen, StatisticsViewModel, PieChart, MonthlyBarChart
  settings/          SettingsScreen, SettingsViewModel, ...
  navigation/        Screen.kt, ExpenseTrackerApp.kt (NavHost + bottom bar)
  theme/             Material 3 color scheme, typography
  common/            Shared composables (TransactionListItem, loading/empty/error states),
                     formatting helpers, and the Category -> icon/color mapping
```

Each screen follows the same MVVM shape:

1. A `*UiState` data class describing everything the screen can render,
   including `isLoading` and `errorMessage` so loading and error states are
   explicit, not implicit.
2. A `*ViewModel` annotated with `@HiltViewModel` that injects use cases
   (never repositories or DAOs directly), combines their flows with
   `combine`/`flatMapLatest`, and exposes the result as a single
   `StateFlow<UiState>` via `stateIn`.
3. A stateless `@Composable` screen function that collects that state with
   `collectAsStateWithLifecycle()` and renders it, delegating every user
   action back to the ViewModel through plain callback lambdas.

This keeps the composables easy to preview and test in isolation, since they
never talk to Room, DataStore or Hilt directly.

## Dependency injection (Hilt)

- `ExpenseTrackerApplication` is annotated with `@HiltAndroidApp` and is the
  DI container root.
- `di/DatabaseModule` provides the singleton `ExpenseDatabase` and
  `TransactionDao`.
- `di/RepositoryModule` binds the domain repository interfaces to their
  `data/repository` implementations with `@Binds`.
- ViewModels are annotated with `@HiltViewModel` and obtained in Compose with
  `hiltViewModel()`, so nothing is wired manually.

## Navigation

`presentation/navigation/ExpenseTrackerApp.kt` hosts a single `NavHost` with
five routes (`dashboard`, `history`, `statistics`, `settings`,
`add_transaction`). A `NavigationBar` and a `FloatingActionButton` (for
adding a transaction) are shown only on the four top-level tabs; the Add
Transaction screen is a full-screen destination reached from the FAB.

## Error handling & loading states

Every ViewModel that reads from Room wraps its flow with `.catch { }` and
maps failures into `errorMessage` on the UI state; every use case that can
fail (`AddTransactionUseCase`, `DeleteTransactionUseCase`) returns a Kotlin
`Result` instead of throwing, so the ViewModel decides how to surface the
failure instead of the app crashing. Screens branch on
`isLoading` / `errorMessage` / empty data to show a spinner, an error
message, an empty state, or the real content.

## Why this structure

This mirrors what you'd see recommended by the official
[Android architecture guidance](https://developer.android.com/topic/architecture)
for a small-to-medium app: enough separation to unit test business rules and
swap persistence without touching the UI, without over-engineering it into
more modules or layers than a single-module app actually needs.
