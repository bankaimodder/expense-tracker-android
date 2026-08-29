package com.expensetracker.app.domain.model

data class DashboardData(
    val totalBalance: Double,
    val monthlyIncome: Double,
    val monthlyExpense: Double,
    val recentTransactions: List<Transaction>
)
