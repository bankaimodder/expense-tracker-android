package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        return runCatching { repository.deleteTransaction(transaction) }
    }
}
