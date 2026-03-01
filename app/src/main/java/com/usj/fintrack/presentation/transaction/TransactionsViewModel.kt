package com.usj.fintrack.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.enum.TransactionType
import com.usj.fintrack.domain.repository.CategoryRepository
import com.usj.fintrack.domain.usecase.account.GetAllAccountsUseCase
import com.usj.fintrack.domain.usecase.transaction.CreateTransactionUseCase
import com.usj.fintrack.domain.usecase.transaction.DeleteTransactionUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionByIdUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionsUseCase
import com.usj.fintrack.domain.usecase.transaction.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                getTransactionsUseCase(),
                getAllAccountsUseCase(),
                categoryRepository.getAllCategories()
            ) { transactions, accounts, categories ->
                Triple(transactions, accounts, categories)
            }.collect { (transactions, accounts, categories) ->
                _uiState.update { current ->
                    current.copy(
                        transactions = transactions,
                        accounts = accounts,
                        categories = categories,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setFilter(type: TransactionType?) {
        _uiState.update { it.copy(selectedFilter = type) }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteTransactionUseCase(id)
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun createTransaction(
        accountId: Long,
        amount: Double,
        type: TransactionType,
        description: String,
        categories: List<com.usj.fintrack.domain.model.Category>,
        date: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transaction = Transaction(
                id = 0,
                accountId = accountId,
                amount = amount,
                type = type,
                date = date,
                description = description,
                categories = categories
            )
            createTransactionUseCase(transaction)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, createSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }

    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }

    fun loadTransactionForEditing(id: Long) {
        viewModelScope.launch {
            val transaction = getTransactionByIdUseCase(id)
            _uiState.update { it.copy(editingTransaction = transaction) }
        }
    }

    fun updateTransaction(
        accountId: Long,
        amount: Double,
        type: TransactionType,
        description: String,
        categories: List<com.usj.fintrack.domain.model.Category>,
        date: Long
    ) {
        val editing = _uiState.value.editingTransaction ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transaction = editing.copy(
                accountId = accountId,
                amount = amount,
                type = type,
                date = date,
                description = description,
                categories = categories
            )
            updateTransactionUseCase(transaction)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, updateSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }

    fun clearUpdateSuccess() {
        _uiState.update { it.copy(updateSuccess = false) }
    }

    fun clearEditingTransaction() {
        _uiState.update { it.copy(editingTransaction = null) }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
