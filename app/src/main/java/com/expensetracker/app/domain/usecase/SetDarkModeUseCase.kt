package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.repository.SettingsRepository
import javax.inject.Inject

class SetDarkModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setDarkMode(enabled)
}
