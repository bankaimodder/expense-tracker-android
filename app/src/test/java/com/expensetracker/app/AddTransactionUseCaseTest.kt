package com.expensetracker.app

import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.usecase.AddTransactionUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class AddTransactionUseCaseTest {

    private lateinit var repository: FakeTransactionRepository
    private lateinit var useCase: AddTransactionUseCase

    @Before
    fun setUp() {
        repository = FakeTransactionRepository()
        useCase = AddTransactionUseCase(repository)
    }

    @Test
    fun `rejects a transaction with zero amount`() = runTest {
        val transaction = Transaction(
            amount = 0.0,
            type = TransactionType.EXPENSE,
            category = Category.FOOD,
            note = "Lunch",
            date = LocalDate.now()
        )

        val result = useCase(transaction)

        assertTrue(result.isFailure)
    }

    @Test
    fun `rejects a transaction with negative amount`() = runTest {
        val transaction = Transaction(
            amount = -15.0,
            type = TransactionType.EXPENSE,
            category = Category.TRANSPORT,
            note = "Bus ticket",
            date = LocalDate.now()
        )

        val result = useCase(transaction)

        assertTrue(result.isFailure)
    }

    @Test
    fun `saves a valid transaction and returns its generated id`() = runTest {
        val transaction = Transaction(
            amount = 42.50,
            type = TransactionType.EXPENSE,
            category = Category.SHOPPING,
            note = "New shoes",
            date = LocalDate.now()
        )

        val result = useCase(transaction)

        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull())
    }
}
