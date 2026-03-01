package com.usj.fintrack.presentation.settings

import com.usj.fintrack.domain.model.User
import com.usj.fintrack.domain.model.UserPreferences

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val user: User? = null,
    val isLoading: Boolean = true
)
