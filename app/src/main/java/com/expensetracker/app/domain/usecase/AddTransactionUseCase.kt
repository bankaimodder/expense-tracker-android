package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        if (transaction.amount <= 0.0) {
            return Result.failure(IllegalArgumentException("Amount must be greater than zero"))
        }
        if (transaction.note.length > 200) {
            return Result.failure(IllegalArgumentException("Note is too long"))
        }
        return runCatching { repository.addTransaction(transaction) }
    }
}
