package com.usj.fintrack.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.usj.fintrack.domain.model.AppTheme
import com.usj.fintrack.domain.model.UserPreferences
import com.usj.fintrack.domain.model.enum.Currency
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val THEME_KEY    = stringPreferencesKey("theme")
        private val CURRENCY_KEY = stringPreferencesKey("currency")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            theme = runCatching {
                AppTheme.valueOf(prefs[THEME_KEY] ?: AppTheme.SYSTEM.name)
            }.getOrDefault(AppTheme.SYSTEM),
            currency = runCatching {
                Currency.valueOf(prefs[CURRENCY_KEY] ?: Currency.EUR.name)
            }.getOrDefault(Currency.EUR)
        )
    }

    suspend fun updateTheme(theme: AppTheme) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }

    suspend fun updateCurrency(currency: Currency) {
        context.dataStore.edit { it[CURRENCY_KEY] = currency.name }
    }
}
