package com.expensetracker.app

import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.usecase.GetDashboardDataUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GetDashboardDataUseCaseTest {

    private lateinit var repository: FakeTransactionRepository
    private lateinit var useCase: GetDashboardDataUseCase

    @Before
    fun setUp() {
        repository = FakeTransactionRepository()
        useCase = GetDashboardDataUseCase(repository)
    }

    @Test
    fun `computes total balance and monthly totals from current month transactions`() = runTest {
        val today = LocalDate.now()

        repository.addTransaction(
            Transaction(amount = 2000.0, type = TransactionType.INCOME, category = Category.SALARY, note = "Paycheck", date = today)
        )
        repository.addTransaction(
            Transaction(amount = 50.0, type = TransactionType.EXPENSE, category = Category.FOOD, note = "Groceries", date = today)
        )
        repository.addTransaction(
            Transaction(amount = 30.0, type = TransactionType.EXPENSE, category = Category.TRANSPORT, note = "Taxi", date = today)
        )

        val dashboardData = useCase().first()

        assertEquals(1920.0, dashboardData.totalBalance, 0.001)
        assertEquals(2000.0, dashboardData.monthlyIncome, 0.001)
        assertEquals(80.0, dashboardData.monthlyExpense, 0.001)
        assertEquals(3, dashboardData.recentTransactions.size)
    }

    @Test
    fun `ignores transactions from previous months when computing monthly totals`() = runTest {
        val lastMonth = LocalDate.now().minusMonths(1)

        repository.addTransaction(
            Transaction(amount = 500.0, type = TransactionType.INCOME, category = Category.SALARY, note = "Old paycheck", date = lastMonth)
        )

        val dashboardData = useCase().first()

        assertEquals(500.0, dashboardData.totalBalance, 0.001)
        assertEquals(0.0, dashboardData.monthlyIncome, 0.001)
    }
}
