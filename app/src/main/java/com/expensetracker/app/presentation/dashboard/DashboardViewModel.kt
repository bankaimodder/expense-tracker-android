package com.expensetracker.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.usecase.GetCurrencyUseCase
import com.expensetracker.app.domain.usecase.GetDashboardDataUseCase
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
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        combine(
            getDashboardDataUseCase(),
            getCurrencyUseCase()
        ) { dashboardData, currency ->
            DashboardUiState(
                isLoading = false,
                errorMessage = null,
                totalBalance = dashboardData.totalBalance,
                monthlyIncome = dashboardData.monthlyIncome,
                monthlyExpense = dashboardData.monthlyExpense,
                recentTransactions = dashboardData.recentTransactions,
                currency = currency
            )
        }.catch { throwable ->
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = throwable.message ?: "Unable to load dashboard data"
            )
        }.onEach { newState ->
            _uiState.value = newState
        }.launchIn(viewModelScope)
    }
}
