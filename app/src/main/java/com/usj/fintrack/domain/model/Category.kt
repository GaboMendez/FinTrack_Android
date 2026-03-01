package com.usj.fintrack.domain.model

/**
 * Domain model for a transaction category (e.g. Food, Transport, Salary).
 *
 * @property id        Local auto-generated primary key.
 * @property name      Display name shown on chips and filters.
 * @property iconName  Material icon name string used by the UI layer to
 *                     resolve an icon without importing Android resources here.
 * @property colorHex  Hex colour string (e.g. "#4CAF50") for tinting the icon
 *                     and chip background.
 * @property isDefault `true` for the seed categories shipped with the app;
 *                     `false` for user-created ones.
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val isDefault: Boolean = false
)
