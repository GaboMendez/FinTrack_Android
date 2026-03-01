package com.usj.fintrack.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.BudgetStatus
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.usecase.budget.CheckBudgetStatusUseCase
import com.usj.fintrack.domain.usecase.budget.DeleteBudgetUseCase
import com.usj.fintrack.domain.usecase.budget.GetBudgetByIdUseCase
import com.usj.fintrack.domain.usecase.category.GetCategoryByIdUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetDetailUiState(
    val budget: Budget? = null,
    val budgetStatus: BudgetStatus? = null,
    val categoryName: String? = null,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false
)

@HiltViewModel
class BudgetDetailViewModel @Inject constructor(
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val checkBudgetStatusUseCase: CheckBudgetStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetDetailUiState(isLoading = true))
    val uiState: StateFlow<BudgetDetailUiState> = _uiState.asStateFlow()

    fun loadBudget(budgetId: Long) {
        viewModelScope.launch {
            try {
                val budget = getBudgetByIdUseCase(budgetId)
                if (budget != null) {
                    val category = getCategoryByIdUseCase(budget.categoryId)
                    _uiState.update {
                        it.copy(budget = budget, categoryName = category?.name, isLoading = false)
                    }
                    observeRelatedTransactions(budget)
                } else {
                    _uiState.update { it.copy(errorMessage = "Budget not found", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    private fun observeRelatedTransactions(budget: Budget) {
        viewModelScope.launch {
            getTransactionsUseCase()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .collect { all ->
                    val filtered = all.filter { tx ->
                        tx.categories.any { cat -> cat.id == budget.categoryId }
                    }
                    // Recompute status on every transaction change so progress bar is always live
                    val status = checkBudgetStatusUseCase(budget, all)
                    _uiState.update { it.copy(transactions = filtered, budgetStatus = status) }
                }
        }
    }

    fun deleteBudget(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteBudgetUseCase(id)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(isDeleted = true, isLoading = false) } }
        }
    }

    fun dismissError() = _uiState.update { it.copy(errorMessage = null) }
}
