package com.usj.fintrack.domain.usecase.category

import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.repository.CategoryRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Long> = runCatching {
        require(category.name.isNotBlank()) { "Category name cannot be blank" }
        categoryRepository.insertCategory(category)
    }
}
