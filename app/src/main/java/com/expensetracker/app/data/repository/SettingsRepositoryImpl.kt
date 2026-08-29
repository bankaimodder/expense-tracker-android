package com.expensetracker.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.expensetracker.app.data.preferences.settingsDataStore
import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode_enabled")
        val CURRENCY = stringPreferencesKey("selected_currency")
    }

    override val isDarkMode: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[Keys.DARK_MODE] ?: false
    }

    override val currency: Flow<Currency> = context.settingsDataStore.data.map { preferences ->
        Currency.fromCode(preferences[Keys.CURRENCY] ?: Currency.USD.code)
    }

    override suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.DARK_MODE] = enabled
        }
    }

    override suspend fun setCurrency(currency: Currency) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.CURRENCY] = currency.code
        }
    }
}
