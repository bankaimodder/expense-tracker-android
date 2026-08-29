package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.model.Currency
import com.expensetracker.app.domain.repository.SettingsRepository
import javax.inject.Inject

class SetCurrencyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(currency: Currency) = repository.setCurrency(currency)
}
