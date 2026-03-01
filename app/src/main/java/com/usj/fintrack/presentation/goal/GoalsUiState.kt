package com.usj.fintrack.presentation.goal

import com.usj.fintrack.domain.model.Goal

data class GoalsUiState(
    val goals: List<Goal> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    /** Goal currently being edited; null when creating a new one. */
    val editingGoal: Goal? = null,
    /** Flipped to true after a successful create; screen resets it and pops back. */
    val createSuccess: Boolean = false,
    /** Flipped to true after a successful update; screen resets it and pops back. */
    val updateSuccess: Boolean = false
)
