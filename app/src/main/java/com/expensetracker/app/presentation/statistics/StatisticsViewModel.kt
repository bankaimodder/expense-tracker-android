package com.expensetracker.app.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.usecase.GetCurrencyUseCase
import com.expensetracker.app.domain.usecase.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getStatisticsUseCase: GetStatisticsUseCase,
    getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    val uiState: StateFlow<StatisticsUiState> = combine(
        getStatisticsUseCase(),
        getCurrencyUseCase()
    ) { statistics, currency ->
        StatisticsUiState(
            isLoading = false,
            categoryBreakdown = statistics.categoryBreakdown,
            monthlySummary = statistics.monthlySummary,
            totalExpenseThisMonth = statistics.totalExpenseThisMonth,
            currency = currency
        )
    }.catch { throwable ->
        emit(
            StatisticsUiState(
                isLoading = false,
                errorMessage = throwable.message ?: "Unable to load statistics"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StatisticsUiState()
    )
}
