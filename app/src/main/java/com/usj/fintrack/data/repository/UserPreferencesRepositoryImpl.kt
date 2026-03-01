package com.usj.fintrack.data.repository

import com.usj.fintrack.data.preferences.UserPreferencesDataSource
import com.usj.fintrack.domain.model.AppTheme
import com.usj.fintrack.domain.model.UserPreferences
import com.usj.fintrack.domain.model.enum.Currency
import com.usj.fintrack.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : UserPreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> = dataSource.userPreferencesFlow

    override suspend fun updateTheme(theme: AppTheme) = dataSource.updateTheme(theme)

    override suspend fun updateCurrency(currency: Currency) = dataSource.updateCurrency(currency)
}
