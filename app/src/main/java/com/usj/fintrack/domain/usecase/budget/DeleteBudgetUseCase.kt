package com.usj.fintrack.domain.usecase.budget

import com.usj.fintrack.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        runCatching { budgetRepository.deleteBudget(id) }
}
