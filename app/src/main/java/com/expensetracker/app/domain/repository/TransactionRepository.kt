package com.expensetracker.app.domain.repository

import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getAllTransactions(): Flow<List<Transaction>>

    fun getFilteredTransactions(query: String, category: Category?): Flow<List<Transaction>>

    suspend fun addTransaction(transaction: Transaction): Long

    suspend fun deleteTransaction(transaction: Transaction)
}
