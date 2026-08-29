package com.expensetracker.app.domain.model

import java.time.LocalDate

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: Category,
    val note: String,
    val date: LocalDate
)
