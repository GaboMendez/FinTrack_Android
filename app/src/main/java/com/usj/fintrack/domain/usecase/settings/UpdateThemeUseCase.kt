package com.usj.fintrack.domain.usecase.settings

import com.usj.fintrack.domain.model.AppTheme
import com.usj.fintrack.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(theme: AppTheme) = repository.updateTheme(theme)
}
