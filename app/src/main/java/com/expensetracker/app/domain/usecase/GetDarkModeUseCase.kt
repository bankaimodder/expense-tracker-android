package com.expensetracker.app.domain.usecase

import com.expensetracker.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isDarkMode
}
