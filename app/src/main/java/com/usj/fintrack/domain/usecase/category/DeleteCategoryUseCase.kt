package com.usj.fintrack.domain.usecase.category

import com.usj.fintrack.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = runCatching {
        categoryRepository.deleteCategory(id)
    }
}
