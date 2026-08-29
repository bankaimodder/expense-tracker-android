package com.expensetracker.app.presentation.statistics

import com.expensetracker.app.domain.model.CategoryBreakdown
import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.model.MonthlySummary

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val categoryBreakdown: List<CategoryBreakdown> = emptyList(),
    val monthlySummary: List<MonthlySummary> = emptyList(),
    val totalExpenseThisMonth: Double = 0.0,
    val currency: Currency = Currency.USD
)
