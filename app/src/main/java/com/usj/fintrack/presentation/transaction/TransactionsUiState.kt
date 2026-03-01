package com.usj.fintrack.presentation.transaction

import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.enum.TransactionType

data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val selectedFilter: TransactionType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
    val createSuccess: Boolean = false,
    val editingTransaction: Transaction? = null,
    val updateSuccess: Boolean = false
) {
    val filteredTransactions: List<Transaction>
        get() = if (selectedFilter == null) transactions
                else transactions.filter { it.type == selectedFilter }
}
