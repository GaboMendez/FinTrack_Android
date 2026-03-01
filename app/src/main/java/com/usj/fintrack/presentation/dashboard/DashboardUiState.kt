package com.usj.fintrack.presentation.dashboard

import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.Transaction

data class DashboardUiState(
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
