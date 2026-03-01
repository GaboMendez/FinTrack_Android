package com.usj.fintrack.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.usecase.budget.CheckBudgetStatusUseCase
import com.usj.fintrack.domain.usecase.budget.CreateBudgetUseCase
import com.usj.fintrack.domain.usecase.budget.DeleteBudgetUseCase
import com.usj.fintrack.domain.usecase.budget.GetBudgetByIdUseCase
import com.usj.fintrack.domain.usecase.budget.GetBudgetsUseCase
import com.usj.fintrack.domain.usecase.budget.UpdateBudgetUseCase
import com.usj.fintrack.domain.usecase.category.GetCategoriesUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val checkBudgetStatusUseCase: CheckBudgetStatusUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetsUiState(isLoading = true))
    val uiState: StateFlow<BudgetsUiState> = _uiState.asStateFlow()

    init {
        observeBudgets()
        observeCategories()
    }

    private fun observeBudgets() {
        viewModelScope.launch {
            combine(
                getBudgetsUseCase(),
                getTransactionsUseCase()
            ) { budgets, transactions -> budgets to transactions }
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { (budgets, transactions) ->
                    val statuses = budgets.associate { budget ->
                        budget.id to checkBudgetStatusUseCase(budget, transactions)
                    }
                    _uiState.update { it.copy(budgets = budgets, budgetStatuses = statuses, isLoading = false) }
                }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .collect { categories ->
                    _uiState.update { it.copy(categories = categories) }
                }
        }
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    fun createBudget(
        categoryId: Long,
        limitAmount: Double,
        periodStartDate: Long,
        periodEndDate: Long
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val budget = Budget(
                userId = 1L, // placeholder until auth is wired
                categoryId = categoryId,
                limitAmount = limitAmount,
                periodStartDate = periodStartDate,
                periodEndDate = periodEndDate
            )
            createBudgetUseCase(budget)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(createSuccess = true, isLoading = false) } }
        }
    }

    fun updateBudget(
        categoryId: Long,
        limitAmount: Double,
        periodStartDate: Long,
        periodEndDate: Long
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val existing = _uiState.value.editingBudget ?: return@launch
            val updated = existing.copy(
                categoryId = categoryId,
                limitAmount = limitAmount,
                periodStartDate = periodStartDate,
                periodEndDate = periodEndDate
            )
            updateBudgetUseCase(updated)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(updateSuccess = true, isLoading = false) } }
        }
    }

    fun deleteBudget(id: Long) {
        viewModelScope.launch {
            deleteBudgetUseCase(id)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    /** Load a budget by [id] via use case and stash it in [BudgetsUiState.editingBudget]. */
    fun loadBudgetForEditing(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val budget = getBudgetByIdUseCase(id)
                _uiState.update { it.copy(editingBudget = budget, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun clearCreateSuccess() = _uiState.update { it.copy(createSuccess = false) }
    fun clearUpdateSuccess() = _uiState.update { it.copy(updateSuccess = false) }
    fun clearEditingBudget() = _uiState.update { it.copy(editingBudget = null) }

    fun dismissError() = _uiState.update { it.copy(errorMessage = null) }
}
