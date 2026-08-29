package com.expensetracker.app.domain.repository

import com.expensetracker.app.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val isDarkMode: Flow<Boolean>

    val currency: Flow<Currency>

    suspend fun setDarkMode(enabled: Boolean)

    suspend fun setCurrency(currency: Currency)
}
