package com.usj.fintrack.domain.model

import com.usj.fintrack.domain.model.enum.Currency

/**
 * Persisted user preferences (theme + currency).
 * Stored via DataStore — no Android/Room dependency.
 */
data class UserPreferences(
    val theme: AppTheme = AppTheme.SYSTEM,
    val currency: Currency = Currency.EUR
)
