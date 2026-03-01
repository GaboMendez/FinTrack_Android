package com.usj.fintrack.domain.model

import com.usj.fintrack.domain.model.enum.Currency

/**
 * Domain model for the user's display profile (1-1 with [User]).
 *
 * @property id                Local auto-generated primary key.
 * @property userOwnerId       Foreign key referencing [User.id].
 * @property name              Display name shown across the app.
 * @property avatarUrl         Optional URL of the user's profile picture.
 * @property currency          Preferred display currency — see [Currency].
 * @property preferredLanguage BCP-47 language tag (e.g. "es", "en").
 */
data class UserProfile(
    val id: Long = 0,
    val userOwnerId: Long,
    val name: String,
    val avatarUrl: String? = null,
    val currency: Currency = Currency.EUR,
    val preferredLanguage: String = "es"
)
