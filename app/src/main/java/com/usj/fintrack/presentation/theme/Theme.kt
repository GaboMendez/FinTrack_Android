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
    primary             = FinTrackGreen40,
    onPrimary           = Color.White,
    primaryContainer    = FinTrackGreen90,
    onPrimaryContainer  = FinTrackGreen10,

    secondary           = SlateBlue40,
    onSecondary         = Color.White,
    secondaryContainer  = SlateBlue90,
    onSecondaryContainer = SlateBlue10,

    tertiary            = Amber40,
    onTertiary          = Color.White,
    tertiaryContainer   = Amber90,
    onTertiaryContainer = Amber10,

    error               = FinTrackRed40,
    onError             = Color.White,
    errorContainer      = FinTrackRed90,
    onErrorContainer    = FinTrackRed10,

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
    inversePrimary      = FinTrackGreen80,

    surfaceTint         = FinTrackGreen40,
    scrim               = Color.Black
)

// ─── Dark color scheme ────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary             = FinTrackGreen80,
    onPrimary           = FinTrackGreen20,
    primaryContainer    = FinTrackGreen30,
    onPrimaryContainer  = FinTrackGreen90,

    secondary           = SlateBlue80,
    onSecondary         = SlateBlue20,
    secondaryContainer  = SlateBlue30,
    onSecondaryContainer = SlateBlue90,

    tertiary            = Amber80,
    onTertiary          = Amber20,
    tertiaryContainer   = Amber30,
    onTertiaryContainer = Amber90,

    error               = FinTrackRed80,
    onError             = FinTrackRed20,
    errorContainer      = FinTrackRed30,
    onErrorContainer    = FinTrackRed90,

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
    inversePrimary      = FinTrackGreen40,

    surfaceTint         = FinTrackGreen80,
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
