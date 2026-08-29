package com.expensetracker.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.usecase.GetCurrencyUseCase
import com.expensetracker.app.domain.usecase.GetDarkModeUseCase
import com.expensetracker.app.domain.usecase.SetCurrencyUseCase
import com.expensetracker.app.domain.usecase.SetDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val setDarkModeUseCase: SetDarkModeUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val setCurrencyUseCase: SetCurrencyUseCase
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        getDarkModeUseCase(),
        getCurrencyUseCase()
    ) { isDarkMode, currency ->
        SettingsUiState(isDarkMode = isDarkMode, currency = currency)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onDarkModeToggled(enabled: Boolean) {
        viewModelScope.launch {
            setDarkModeUseCase(enabled)
        }
    }

    fun onCurrencySelected(currency: Currency) {
        viewModelScope.launch {
            setCurrencyUseCase(currency)
        }
    }
}
