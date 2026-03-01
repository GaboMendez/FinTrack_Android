package com.usj.fintrack.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.usecase.account.DeleteAccountUseCase
import com.usj.fintrack.domain.usecase.account.GetAccountByIdUseCase
import com.usj.fintrack.domain.usecase.transaction.GetTransactionsByAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountDetailUiState(
    val account: Account? = null,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false
)

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountDetailUiState(isLoading = true))
    val uiState: StateFlow<AccountDetailUiState> = _uiState.asStateFlow()

    fun loadAccount(accountId: Long) {
        viewModelScope.launch {
            try {
                val account = getAccountByIdUseCase(accountId)
                _uiState.update { it.copy(account = account, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }

        viewModelScope.launch {
            getTransactionsByAccountUseCase(accountId)
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .collect { transactions ->
                    _uiState.update { it.copy(transactions = transactions) }
                }
        }
    }

    fun deleteAccount(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            deleteAccountUseCase(id)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(isDeleted = true, isLoading = false) } }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
