package com.usj.fintrack.domain.model

/**
 * Domain model representing an application user.
 *
 * @property id         Local auto-generated primary key.
 * @property email      User's email address.
 * @property createdAt  Unix epoch milliseconds when the user was created.
 */
data class User(
    val id: Long = 0,
    val email: String,
    val createdAt: Long
)
