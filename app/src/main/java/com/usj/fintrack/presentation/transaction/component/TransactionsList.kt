package com.usj.fintrack.presentation.transaction.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.presentation.component.TransactionListItem

@Composable
fun TransactionsList(
    transactions: List<Transaction>,
    onTransactionClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    accounts: List<Account> = emptyList(),
    onTransactionEdit: ((Long) -> Unit)? = null,
    onTransactionDelete: ((Long) -> Unit)? = null
) {
    if (transactions.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No transactions yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(
                items = transactions,
                key = { it.id }
            ) { transaction ->
                TransactionListItem(
                    transaction = transaction,
                    onClick = { onTransactionClick(transaction.id) },
                    accountName = accounts.find { it.id == transaction.accountId }?.name,
                    onEdit = onTransactionEdit?.let { cb -> { cb(transaction.id) } },
                    onDelete = onTransactionDelete?.let { cb -> { cb(transaction.id) } }
                )
                HorizontalDivider()
            }
        }
    }
}
