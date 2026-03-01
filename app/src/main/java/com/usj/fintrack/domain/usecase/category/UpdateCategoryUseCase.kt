package com.usj.fintrack.domain.usecase.category

import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Unit> = runCatching {
        require(category.id > 0L) { "Cannot update a category without an existing ID" }
        require(category.name.isNotBlank()) { "Category name cannot be blank" }
        categoryRepository.updateCategory(category)
    }
}
