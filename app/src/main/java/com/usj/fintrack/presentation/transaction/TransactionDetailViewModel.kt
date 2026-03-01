package com.usj.fintrack.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.usecase.account.GetAccountByIdUseCase
import com.usj.fintrack.domain.usecase.transaction.DeleteTransactionUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionDetailUiState(
    val transaction: Transaction? = null,
    val accountName: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false
)

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionDetailUiState())
    val uiState: StateFlow<TransactionDetailUiState> = _uiState.asStateFlow()

    fun loadTransaction(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transaction = getTransactionByIdUseCase(id)
            val accountName = transaction?.let { getAccountByIdUseCase(it.accountId)?.name }
            _uiState.update { it.copy(transaction = transaction, accountName = accountName, isLoading = false) }
        }
    }

    fun deleteTransaction() {
        val id = _uiState.value.transaction?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteTransactionUseCase(id)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isDeleted = true) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
