package com.expensetracker.app.presentation.history

import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.model.Transaction

data class HistoryUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val transactions: List<Transaction> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val currency: Currency = Currency.USD
)
