package com.expensetracker.app.presentation.settings

import com.expensetracker.app.domain.model.Currency

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val currency: Currency = Currency.USD
)
