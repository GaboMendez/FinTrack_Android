package com.usj.fintrack.presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.usecase.goal.AddGoalContributionUseCase
import com.usj.fintrack.domain.usecase.goal.DeleteGoalUseCase
import com.usj.fintrack.domain.usecase.goal.GetGoalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalDetailUiState(
    val goal: Goal? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false,
    /** Flipped to true when a contribution is successfully added. */
    val contributionAdded: Boolean = false
)

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    private val getGoalsUseCase: GetGoalsUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val addGoalContributionUseCase: AddGoalContributionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalDetailUiState(isLoading = true))
    val uiState: StateFlow<GoalDetailUiState> = _uiState.asStateFlow()

    private var currentGoalId: Long = -1L

    fun loadGoal(goalId: Long) {
        currentGoalId = goalId
        viewModelScope.launch {
            getGoalsUseCase()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { goals ->
                    val goal = goals.find { it.id == goalId }
                    if (goal != null) {
                        _uiState.update { it.copy(goal = goal, isLoading = false) }
                    } else {
                        _uiState.update { it.copy(errorMessage = "Goal not found", isLoading = false) }
                    }
                }
        }
    }

    fun addContribution(amount: Double) {
        val goalId = currentGoalId
        if (goalId < 0) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            addGoalContributionUseCase(goalId, amount)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(contributionAdded = true, isLoading = false) } }
        }
    }

    fun deleteGoal() {
        val goalId = currentGoalId
        if (goalId < 0) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteGoalUseCase(goalId)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(isDeleted = true, isLoading = false) } }
        }
    }

    fun resetContributionAdded() = _uiState.update { it.copy(contributionAdded = false) }
    fun dismissError() = _uiState.update { it.copy(errorMessage = null) }
}
