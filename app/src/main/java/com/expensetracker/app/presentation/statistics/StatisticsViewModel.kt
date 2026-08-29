package com.expensetracker.app.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.usecase.GetCurrencyUseCase
import com.expensetracker.app.domain.usecase.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatisticsUseCase: GetStatisticsUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        combine(
            getStatisticsUseCase(),
            getCurrencyUseCase()
        ) { statistics, currency ->
            StatisticsUiState(
                isLoading = false,
                errorMessage = null,
                categoryBreakdown = statistics.categoryBreakdown,
                monthlySummary = statistics.monthlySummary,
                totalExpenseThisMonth = statistics.totalExpenseThisMonth,
                currency = currency
            )
        }.catch { throwable ->
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = throwable.message ?: "Unable to load statistics"
            )
        }.onEach { newState ->
            _uiState.value = newState
        }.launchIn(viewModelScope)
    }
}
