package com.usj.fintrack.presentation.dashboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.presentation.component.TransactionListItem

@Composable
fun RecentTransactionsList(
    transactions: List<Transaction>,
    onTransactionClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        transactions.take(5).forEachIndexed { index, transaction ->
            TransactionListItem(
                transaction = transaction,
                onClick = { onTransactionClick(transaction.id) }
            )
            if (index < transactions.lastIndex) {
                HorizontalDivider()
            }
        }
        if (transactions.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
