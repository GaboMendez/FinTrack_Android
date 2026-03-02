package com.usj.fintrack.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ─── Light color scheme ───────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary             = BrandRed40,
    onPrimary           = Color.White,
    primaryContainer    = BrandRed90,
    onPrimaryContainer  = BrandRed10,

    secondary           = BrandOrange40,
    onSecondary         = Color.White,
    secondaryContainer  = BrandOrange90,
    onSecondaryContainer = BrandOrange10,

    tertiary            = BrandYellow40,
    onTertiary          = Color.Black,
    tertiaryContainer   = BrandYellow90,
    onTertiaryContainer = BrandYellow10,

    error               = BrandError40,
    onError             = Color.White,
    errorContainer      = BrandError90,
    onErrorContainer    = BrandRed10,

    background          = Neutral99,
    onBackground        = Neutral10,

    surface             = Neutral99,
    onSurface           = Neutral10,
    surfaceVariant      = NeutralVariant90,
    onSurfaceVariant    = NeutralVariant30,

    outline             = NeutralVariant50,
    outlineVariant      = NeutralVariant80,

    inverseSurface      = Neutral20,
    inverseOnSurface    = Neutral95,
    inversePrimary      = BrandRed80,

    surfaceTint         = BrandRed40,
    scrim               = Color.Black
)
// ─── Dark color scheme ────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary             = BrandRed80,
    onPrimary           = BrandRed20,
    primaryContainer    = BrandRed30,
    onPrimaryContainer  = BrandRed90,

    secondary           = BrandOrange80,
    onSecondary         = BrandOrange20,
    secondaryContainer  = BrandOrange30,
    onSecondaryContainer = BrandOrange90,

    tertiary            = BrandYellow80,
    onTertiary          = Color.Black,
    tertiaryContainer   = BrandYellow30,
    onTertiaryContainer = BrandYellow90,

    error               = BrandError80,
    onError             = BrandRed20,
    errorContainer      = BrandRed30,
    onErrorContainer    = BrandRed90,

    background          = Neutral10,
    onBackground        = Neutral90,

    surface             = Neutral10,
    onSurface           = Neutral90,
    surfaceVariant      = NeutralVariant30,
    onSurfaceVariant    = NeutralVariant80,

    outline             = NeutralVariant60,
    outlineVariant      = NeutralVariant30,

    inverseSurface      = Neutral90,
    inverseOnSurface    = Neutral20,
    inversePrimary      = BrandRed40,

    surfaceTint         = BrandRed80,
    scrim               = Color.Black
)
// ─── Extended colors (income / expense semantic tokens) ───────────────────────

/**
 * Semantic colors not covered by Material3's standard scheme.
 * Access via `LocalFinTrackColors.current`.
 */
@Immutable
data class FinTrackExtendedColors(
    /** Amount text for income transactions */
    val income: Color,
    /** Amount text for expense transactions */
    val expense: Color,
    /** Badge / chip container for income */
    val incomeContainer: Color,
    /** Badge / chip container for expense */
    val expenseContainer: Color,
    /** Text on incomeContainer */
    val onIncomeContainer: Color,
    /** Text on expenseContainer */
    val onExpenseContainer: Color
)

private val LightExtendedColors = FinTrackExtendedColors(
    income             = IncomeGreen,
    expense            = ExpenseRed,
    incomeContainer    = IncomeGreenContainer,
    expenseContainer   = ExpenseRedContainer,
    onIncomeContainer  = FinTrackGreen10,
    onExpenseContainer = FinTrackRed10
)

private val DarkExtendedColors = FinTrackExtendedColors(
    income             = IncomeGreenDark,
    expense            = ExpenseRedDark,
    incomeContainer    = FinTrackGreen30,
    expenseContainer   = FinTrackRed30,
    onIncomeContainer  = FinTrackGreen90,
    onExpenseContainer = FinTrackRed90
)

val LocalFinTrackColors = staticCompositionLocalOf { LightExtendedColors }

// ─── Theme composable ─────────────────────────────────────────────────────────

/**
 * FinTrack Material3 theme.
 *
 * Dynamic color is intentionally disabled so the financial color palette
 * (income greens / expense reds) always renders consistently regardless of
 * the device wallpaper.
 *
 * @param darkTheme  Follow system dark-mode setting by default.
 * @param content    The composable hierarchy to theme.
 */
@Composable
fun FinTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalFinTrackColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = FinTrackTypography,
            shapes      = FinTrackShapes,
            content     = content
        )
    }
}

/** Convenience accessor — use like `FinTrackTheme.colors.income` */
object FinTrackTheme {
    val colors: FinTrackExtendedColors
        @Composable get() = LocalFinTrackColors.current
}
