package com.expensetracker.app.domain.model

import java.time.YearMonth

data class CategoryBreakdown(
    val category: Category,
    val total: Double,
    val percentage: Float
)

data class MonthlySummary(
    val yearMonth: YearMonth,
    val income: Double,
    val expense: Double
)

data class Statistics(
    val categoryBreakdown: List<CategoryBreakdown>,
    val monthlySummary: List<MonthlySummary>,
    val totalExpenseThisMonth: Double
)
