package com.usj.fintrack.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.usj.fintrack.domain.model.enum.TransactionType
import com.usj.fintrack.presentation.theme.ExpenseRed
import com.usj.fintrack.presentation.theme.IncomeGreen
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol

@Composable
fun FinancialCard(
    title: String,
    amount: Double,
    type: TransactionType,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            val amountColor = if (type == TransactionType.INCOME) IncomeGreen else ExpenseRed
            val sign = if (type == TransactionType.INCOME) "+" else "-"
            val sym = LocalCurrencySymbol.current
            Text(
                text = "$sign$sym%.2f".format(amount),
                style = MaterialTheme.typography.headlineSmall,
                color = amountColor
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
