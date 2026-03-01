package com.usj.fintrack.presentation.budget

import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.BudgetStatus
import com.usj.fintrack.domain.model.Category

data class BudgetsUiState(
    val budgets: List<Budget> = emptyList(),
    val budgetStatuses: Map<Long, BudgetStatus> = emptyMap(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val editingBudget: Budget? = null,
    /** Flipped to true after a successful create; screen resets it and pops back. */
    val createSuccess: Boolean = false,
    /** Flipped to true after a successful update; screen resets it and pops back. */
    val updateSuccess: Boolean = false
)
