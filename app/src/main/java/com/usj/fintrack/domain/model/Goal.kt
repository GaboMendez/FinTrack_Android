package com.usj.fintrack.domain.model

/**
 * Domain model for a savings goal.
 *
 * @property id            Local auto-generated primary key.
 * @property userId        Foreign key referencing [User.id].
 * @property name          Goal description (e.g. "Vacation fund", "New laptop").
 * @property targetAmount  Total amount the user wants to save.
 * @property currentAmount Amount saved so far (grows via contributions).
 * @property deadline      Unix epoch milliseconds target completion date.
 * @property isCompleted   `true` once [currentAmount] >= [targetAmount].
 */
data class Goal(
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: Long,
    val isCompleted: Boolean = false
)
