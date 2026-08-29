package com.expensetracker.app.presentation.addtransaction

import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.model.TransactionType
import java.time.LocalDate

data class AddTransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    val amountText: String = "",
    val note: String = "",
    val date: LocalDate = LocalDate.now(),
    val selectedCategory: Category = Category.FOOD,
    val availableCategories: List<Category> = Category.categoriesFor(TransactionType.EXPENSE),
    val currency: Currency = Currency.USD,
    val amountError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)
