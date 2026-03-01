package com.usj.fintrack.domain.repository

import com.usj.fintrack.domain.model.AppTheme
import com.usj.fintrack.domain.model.UserPreferences
import com.usj.fintrack.domain.model.enum.Currency
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun updateTheme(theme: AppTheme)
    suspend fun updateCurrency(currency: Currency)
}
