package com.usj.fintrack.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.model.enum.AccountType
import com.usj.fintrack.domain.model.enum.Currency
import com.usj.fintrack.domain.usecase.account.CalculateTotalBalanceUseCase
import com.usj.fintrack.domain.usecase.account.CreateAccountUseCase
import com.usj.fintrack.domain.usecase.account.DeleteAccountUseCase
import com.usj.fintrack.domain.usecase.account.GetAccountByIdUseCase
import com.usj.fintrack.domain.usecase.account.GetAllAccountsUseCase
import com.usj.fintrack.domain.usecase.account.UpdateAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val calculateTotalBalanceUseCase: CalculateTotalBalanceUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountsUiState(isLoading = true))
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    init {
        observeAccounts()
        observeTotalBalance()
    }

    private fun observeAccounts() {
        viewModelScope.launch {
            getAllAccountsUseCase()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { accounts ->
                    _uiState.update { it.copy(accounts = accounts, isLoading = false) }
                }
        }
    }

    private fun observeTotalBalance() {
        viewModelScope.launch {
            calculateTotalBalanceUseCase()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .collect { balance ->
                    _uiState.update { it.copy(totalBalance = balance) }
                }
        }
    }

    fun createAccount(
        name: String,
        type: AccountType,
        initialBalance: Double,
        currency: Currency
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val account = Account(
                userId = 1L, // placeholder until auth is wired
                name = name,
                type = type,
                balance = initialBalance,
                currency = currency,
                createdAt = System.currentTimeMillis()
            )
            createAccountUseCase(account)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(createSuccess = true, isLoading = false) } }
        }
    }

    fun clearCreateSuccess() {
        _uiState.update { it.copy(createSuccess = false) }
    }

    fun deleteAccount(id: Long) {
        viewModelScope.launch {
            deleteAccountUseCase(id)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    /** Load the account to edit into state so [CreateAccountScreen] can pre-populate its fields. */
    fun loadAccountForEditing(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val account = getAccountByIdUseCase(id)
                _uiState.update { it.copy(editingAccount = account, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
        }
    }

    fun updateAccount(
        id: Long,
        name: String,
        type: AccountType,
        balance: Double,
        currency: Currency
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val existing = _uiState.value.editingAccount ?: return@launch
            val updated = existing.copy(name = name, type = type, balance = balance, currency = currency)
            updateAccountUseCase(updated)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .onSuccess { _uiState.update { it.copy(updateSuccess = true, isLoading = false) } }
        }
    }

    fun clearUpdateSuccess() {
        _uiState.update { it.copy(updateSuccess = false) }
    }

    fun clearEditingAccount() {
        _uiState.update { it.copy(editingAccount = null) }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
