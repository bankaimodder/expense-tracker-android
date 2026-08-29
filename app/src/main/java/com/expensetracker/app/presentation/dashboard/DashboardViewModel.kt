package com.expensetracker.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.usecase.GetCurrencyUseCase
import com.expensetracker.app.domain.usecase.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getDashboardDataUseCase: GetDashboardDataUseCase,
    getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        getDashboardDataUseCase(),
        getCurrencyUseCase()
    ) { dashboardData, currency ->
        DashboardUiState(
            isLoading = false,
            totalBalance = dashboardData.totalBalance,
            monthlyIncome = dashboardData.monthlyIncome,
            monthlyExpense = dashboardData.monthlyExpense,
            recentTransactions = dashboardData.recentTransactions,
            currency = currency
        )
    }.catch { throwable ->
        emit(
            DashboardUiState(
                isLoading = false,
                errorMessage = throwable.message ?: "Unable to load dashboard data"
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )
}
