package com.expensetracker.app.presentation.dashboard

import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.model.Transaction

data class DashboardUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val currency: Currency = Currency.USD
)
