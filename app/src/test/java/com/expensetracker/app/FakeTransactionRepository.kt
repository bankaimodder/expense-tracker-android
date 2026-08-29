package com.expensetracker.app

import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeTransactionRepository : TransactionRepository {

    private val transactionsFlow = MutableStateFlow<List<Transaction>>(emptyList())
    private var nextId = 1L

    override fun getAllTransactions(): Flow<List<Transaction>> = transactionsFlow

    override fun getFilteredTransactions(query: String, category: Category?): Flow<List<Transaction>> {
        return transactionsFlow.map { transactions ->
            transactions.filter { transaction ->
                (category == null || transaction.category == category) &&
                    transaction.note.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction? =
        transactionsFlow.value.find { it.id == id }

    override suspend fun addTransaction(transaction: Transaction): Long {
        val id = nextId++
        transactionsFlow.value = transactionsFlow.value + transaction.copy(id = id)
        return id
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionsFlow.value = transactionsFlow.value.filterNot { it.id == transaction.id }
    }
}
