package com.expensetracker.app.presentation.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.model.Category
import com.expensetracker.app.domain.model.Transaction
import com.expensetracker.app.domain.model.TransactionType
import com.expensetracker.app.domain.usecase.AddTransactionUseCase
import com.expensetracker.app.domain.usecase.GetCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        getCurrencyUseCase()
            .onEach { currency -> _uiState.update { it.copy(currency = currency) } }
            .launchIn(viewModelScope)
    }

    fun onTypeSelected(type: TransactionType) {
        val categories = Category.categoriesFor(type)
        _uiState.value = _uiState.value.copy(
            type = type,
            availableCategories = categories,
            selectedCategory = categories.first()
        )
    }

    fun onAmountChanged(text: String) {
        if (text.isEmpty() || text.matches(Regex("^\\d{0,9}(\\.\\d{0,2})?$"))) {
            _uiState.value = _uiState.value.copy(amountText = text, amountError = null)
        }
    }

    fun onNoteChanged(text: String) {
        _uiState.value = _uiState.value.copy(note = text)
    }

    fun onCategorySelected(category: Category) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(date = date)
    }

    fun saveTransaction() {
        val state = _uiState.value
        val amount = state.amountText.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            _uiState.value = state.copy(amountError = "Enter a valid amount")
            return
        }

        _uiState.value = state.copy(isSaving = true, errorMessage = null)

        viewModelScope.launch {
            val transaction = Transaction(
                amount = amount,
                type = state.type,
                category = state.selectedCategory,
                note = state.note.trim(),
                date = state.date
            )
            val result = addTransactionUseCase(transaction)
            result.fold(
                onSuccess = {
                    _uiState.value = AddTransactionUiState(isSaved = true, currency = state.currency)
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "Could not save transaction"
                    )
                }
            )
        }
    }
}
