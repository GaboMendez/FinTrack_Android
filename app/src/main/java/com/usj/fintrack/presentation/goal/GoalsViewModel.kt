package com.usj.fintrack.presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.usecase.goal.AddGoalContributionUseCase
import com.usj.fintrack.domain.usecase.goal.CreateGoalUseCase
import com.usj.fintrack.domain.usecase.goal.DeleteGoalUseCase
import com.usj.fintrack.domain.usecase.goal.GetGoalByIdUseCase
import com.usj.fintrack.domain.usecase.goal.GetGoalsUseCase
import com.usj.fintrack.domain.usecase.goal.UpdateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoalsUseCase: GetGoalsUseCase,
    private val createGoalUseCase: CreateGoalUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val addGoalContributionUseCase: AddGoalContributionUseCase,
    private val getGoalByIdUseCase: GetGoalByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState(isLoading = true))
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        observeGoals()
    }

    private fun observeGoals() {
        viewModelScope.launch {
            getGoalsUseCase()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { goals ->
                    _uiState.update { it.copy(goals = goals, isLoading = false) }
                }
        }
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    fun createGoal(name: String, targetAmount: Double, deadline: Long) {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Goal name cannot be blank.") }
            return
        }
        val nameTaken = _uiState.value.goals.any { it.name.equals(trimmedName, ignoreCase = true) }
        if (nameTaken) {
            _uiState.update { it.copy(errorMessage = "A goal named \"$trimmedName\" already exists.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val goal = Goal(
                userId = 1L, // placeholder until auth is wired
                name = trimmedName,
                targetAmount = targetAmount,
                deadline = deadline
            )
            createGoalUseCase(goal)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(createSuccess = true, isLoading = false) } }
        }
    }

    fun updateGoal(name: String, targetAmount: Double, deadline: Long) {
        val trimmedName = name.trim()
        val existing = _uiState.value.editingGoal ?: return
        if (trimmedName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Goal name cannot be blank.") }
            return
        }
        val nameTaken = _uiState.value.goals.any {
            it.name.equals(trimmedName, ignoreCase = true) && it.id != existing.id
        }
        if (nameTaken) {
            _uiState.update { it.copy(errorMessage = "A goal named \"$trimmedName\" already exists.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val updated = existing.copy(
                name = trimmedName,
                targetAmount = targetAmount,
                deadline = deadline
            )
            updateGoalUseCase(updated)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(updateSuccess = true, isLoading = false) } }
        }
    }

    fun deleteGoal(id: Long) {
        viewModelScope.launch {
            deleteGoalUseCase(id)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    fun addContribution(goalId: Long, amount: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            addGoalContributionUseCase(goalId, amount)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(isLoading = false) } }
        }
    }

    // ── Edit-mode helpers ─────────────────────────────────────────────────────

    fun loadGoalForEditing(id: Long) {
        viewModelScope.launch {
            val goal = getGoalByIdUseCase(id)
            _uiState.update { it.copy(editingGoal = goal) }
        }
    }

    fun clearEditingGoal() = _uiState.update { it.copy(editingGoal = null) }

    // ── Error / success resets ────────────────────────────────────────────────

    fun dismissError() = _uiState.update { it.copy(errorMessage = null) }
    fun resetCreateSuccess() = _uiState.update { it.copy(createSuccess = false) }
    fun resetUpdateSuccess() = _uiState.update { it.copy(updateSuccess = false) }
}
