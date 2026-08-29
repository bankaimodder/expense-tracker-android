package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(query: String = "", category: Category? = null): Flow<List<Transaction>> {
        return repository.getFilteredTransactions(query, category)
    }
}
