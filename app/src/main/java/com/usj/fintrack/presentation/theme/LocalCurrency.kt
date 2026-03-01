package com.usj.fintrack.presentation.theme

import androidx.compose.runtime.compositionLocalOf

/**
 * CompositionLocal that holds the currency symbol currently selected by the user.
 *
 * Provided at the root [MainActivity] level from [SettingsViewModel].
 * All composables use `LocalCurrencySymbol.current` instead of hardcoding "€".
 */
val LocalCurrencySymbol = compositionLocalOf { "€" }
