package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.DashboardData
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<DashboardData> {
        return repository.getAllTransactions().map { transactions ->
            val currentMonth = YearMonth.now()
            val transactionsThisMonth = transactions.filter {
                YearMonth.from(it.date) == currentMonth
            }

            val monthlyIncome = transactionsThisMonth
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }

            val monthlyExpense = transactionsThisMonth
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            val totalBalance = transactions.sumOf {
                if (it.type == TransactionType.INCOME) it.amount else -it.amount
            }

            DashboardData(
                totalBalance = totalBalance,
                monthlyIncome = monthlyIncome,
                monthlyExpense = monthlyExpense,
                recentTransactions = sortedByDateDescending(transactions).take(5)
            )
        }
    }

    private fun sortedByDateDescending(transactions: List<Transaction>): List<Transaction> =
        transactions.sortedWith(compareByDescending<Transaction> { it.date }.thenByDescending { it.id })
}
