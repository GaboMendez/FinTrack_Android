package com.usj.fintrack.domain.usecase.seed

import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.model.User
import com.usj.fintrack.domain.repository.CategoryRepository
import com.usj.fintrack.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SeedDatabaseUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke() {
        seedDefaultUser()
        seedDefaultCategories()
    }

    // ─── Default User ─────────────────────────────────────────────────────────

    private suspend fun seedDefaultUser() {
        if (userRepository.getUserById(1L) == null) {
            userRepository.insertUser(
                User(
                    id = 0,                              // autoGenerate → Room assigns id = 1
                    email = "profesor@usj.com",
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    // ─── Default Categories ───────────────────────────────────────────────────

    private suspend fun seedDefaultCategories() {
        val hasDefaults = categoryRepository
            .getAllCategories()
            .first()
            .any { it.isDefault }

        if (!hasDefaults) {
            categoryRepository.insertDefaultCategories(defaultCategories())
        }
    }

    private fun defaultCategories() = listOf(
        Category(name = "Food",          iconName = "restaurant",     colorHex = "#FF9800", isDefault = true), // Orange
        Category(name = "Transport",     iconName = "directions_car", colorHex = "#2196F3", isDefault = true), // Blue
        Category(name = "Entertainment", iconName = "movie",          colorHex = "#9C27B0", isDefault = true), // Purple
        Category(name = "Health",        iconName = "favorite",       colorHex = "#E91E63", isDefault = true), // Pink
        Category(name = "Housing",       iconName = "home",           colorHex = "#4CAF50", isDefault = true), // Green
        Category(name = "Salary",        iconName = "attach_money",   colorHex = "#009688", isDefault = true), // Teal
        Category(name = "Other",         iconName = "category",       colorHex = "#607D8B", isDefault = true), // Blue-grey
    )
}
