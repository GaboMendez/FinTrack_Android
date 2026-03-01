package com.usj.fintrack.presentation.account

import com.usj.fintrack.domain.model.Account

data class AccountsUiState(
    val accounts: List<Account> = emptyList(),
    val totalBalance: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    /** Populated when navigating to the edit screen; cleared on exit. */
    val editingAccount: Account? = null,
    /** Flipped to true after a successful create; screen resets it and pops back. */
    val createSuccess: Boolean = false,
    /** Flipped to true after a successful update; screen resets it and pops back. */
    val updateSuccess: Boolean = false
)
