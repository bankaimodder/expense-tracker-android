package com.expensetracker.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val SETTINGS_PREFERENCES_NAME = "expense_tracker_settings"

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCES_NAME
)
