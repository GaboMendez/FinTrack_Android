package com.usj.fintrack.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.AppTheme
import com.usj.fintrack.domain.model.enum.Currency
import com.usj.fintrack.domain.usecase.settings.GetCurrentUserUseCase
import com.usj.fintrack.domain.usecase.settings.GetUserPreferencesUseCase
import com.usj.fintrack.domain.usecase.settings.UpdateCurrencyUseCase
import com.usj.fintrack.domain.usecase.settings.UpdateThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserPreferencesUseCase().collect { prefs ->
                _uiState.update { it.copy(preferences = prefs, isLoading = false) }
            }
        }
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            _uiState.update { it.copy(user = user) }
        }
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch { updateThemeUseCase(theme) }
    }

    fun updateCurrency(currency: Currency) {
        viewModelScope.launch { updateCurrencyUseCase(currency) }
    }
}
