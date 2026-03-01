package com.usj.fintrack.domain.usecase.category

import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> = categoryRepository.getAllCategories()
}
