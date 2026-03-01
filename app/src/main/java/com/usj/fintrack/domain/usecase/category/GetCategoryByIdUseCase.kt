package com.usj.fintrack.domain.usecase.category

import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: Long): Category? = categoryRepository.getCategoryById(id)
}
