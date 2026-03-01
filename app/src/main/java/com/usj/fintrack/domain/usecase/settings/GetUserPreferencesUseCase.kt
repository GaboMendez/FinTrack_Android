package com.usj.fintrack.domain.usecase.settings

import com.usj.fintrack.domain.model.UserPreferences
import com.usj.fintrack.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserPreferences> = repository.getUserPreferences()
}
