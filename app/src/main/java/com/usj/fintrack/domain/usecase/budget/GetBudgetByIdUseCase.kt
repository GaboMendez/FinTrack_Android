package com.usj.fintrack.domain.usecase.budget

import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetByIdUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(id: Long): Budget? = budgetRepository.getBudgetById(id)
}
