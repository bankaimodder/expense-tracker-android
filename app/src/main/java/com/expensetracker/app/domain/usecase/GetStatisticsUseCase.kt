package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.CategoryBreakdown
import com.expensetracker.app.domain.model.MonthlySummary
import com.expensetracker.app.domain.model.Statistics
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

class GetStatisticsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<Statistics> {
        return repository.getAllTransactions().map { transactions ->
            val currentMonth = YearMonth.now()
            val expensesThisMonth = transactions.filter {
                it.type == TransactionType.EXPENSE && YearMonth.from(it.date) == currentMonth
            }
            val totalExpense = expensesThisMonth.sumOf { it.amount }

            val breakdown = expensesThisMonth
                .groupBy { it.category }
                .map { (category, items) ->
                    val total = items.sumOf { it.amount }
                    CategoryBreakdown(
                        category = category,
                        total = total,
                        percentage = if (totalExpense > 0) (total / totalExpense * 100).toFloat() else 0f
                    )
                }
                .sortedByDescending { it.total }

            val monthlySummary = (5 downTo 0).map { monthsAgo ->
                val month = currentMonth.minusMonths(monthsAgo.toLong())
                val transactionsInMonth = transactions.filter { YearMonth.from(it.date) == month }
                MonthlySummary(
                    yearMonth = month,
                    income = transactionsInMonth
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount },
                    expense = transactionsInMonth
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                )
            }

            Statistics(
                categoryBreakdown = breakdown,
                monthlySummary = monthlySummary,
                totalExpenseThisMonth = totalExpense
            )
        }
    }
}
