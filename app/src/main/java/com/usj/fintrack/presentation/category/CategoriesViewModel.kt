package com.usj.fintrack.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.usecase.category.CreateCategoryUseCase
import com.usj.fintrack.domain.usecase.category.DeleteCategoryUseCase
import com.usj.fintrack.domain.usecase.category.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState(isLoading = true))
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        observeCategories()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { categories ->
                    _uiState.update { it.copy(categories = categories, isLoading = false) }
                }
        }
    }

    /** Create a brand-new category. */
    fun createCategory(name: String, colorHex: String) {
        viewModelScope.launch {
            val category = Category(name = name, iconName = "label", colorHex = colorHex)
            createCategoryUseCase(category)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    /**
     * Update an existing category by replacing it (Room uses REPLACE strategy).
     * The [id] and [isDefault] flag are preserved.
     */
    fun updateCategory(existing: Category, name: String, colorHex: String) {
        viewModelScope.launch {
            val updated = existing.copy(name = name, colorHex = colorHex)
            createCategoryUseCase(updated)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    /** Delete a user-created category (default categories should not be deleted from the UI). */
    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            deleteCategoryUseCase(id)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    fun dismissError() = _uiState.update { it.copy(errorMessage = null) }
}
