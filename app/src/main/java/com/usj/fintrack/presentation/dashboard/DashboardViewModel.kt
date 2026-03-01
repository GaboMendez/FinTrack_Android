package com.usj.fintrack.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.usecase.account.CalculateTotalBalanceUseCase
import com.usj.fintrack.domain.usecase.account.GetAllAccountsUseCase
import com.usj.fintrack.domain.usecase.budget.GetBudgetsUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionStatsUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val calculateTotalBalanceUseCase: CalculateTotalBalanceUseCase,
    private val getTransactionStatsUseCase: GetTransactionStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        // Total balance via Flow
        viewModelScope.launch {
            calculateTotalBalanceUseCase()
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
                .collect { balance ->
                    _uiState.update { it.copy(totalBalance = balance) }
                }
        }

        // Monthly income / expense stats
        viewModelScope.launch {
            try {
                val (startOfMonth, now) = currentMonthRange()
                val stats = getTransactionStatsUseCase(startOfMonth, now)
                _uiState.update {
                    it.copy(
                        monthlyIncome = stats.totalIncome,
                        monthlyExpense = stats.totalExpense
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }

        // Recent transactions (last 5)
        viewModelScope.launch {
            getTransactionsUseCase()
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
                .collect { transactions ->
                    _uiState.update {
                        it.copy(
                            recentTransactions = transactions.take(5),
                            isLoading = false
                        )
                    }
                }
        }

        // Budgets (first 3)
        viewModelScope.launch {
            getBudgetsUseCase()
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
                .collect { budgets ->
                    _uiState.update { it.copy(budgets = budgets.take(3)) }
                }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun currentMonthRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfMonth = calendar.timeInMillis
        val now = System.currentTimeMillis()
        return startOfMonth to now
    }
}
