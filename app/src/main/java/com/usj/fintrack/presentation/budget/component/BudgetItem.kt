package com.usj.fintrack.presentation.budget.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.BudgetStatus
import com.usj.fintrack.domain.model.enum.BudgetStatusType
import com.usj.fintrack.presentation.component.BudgetProgressBar
import com.usj.fintrack.presentation.theme.Amber40
import com.usj.fintrack.presentation.theme.ExpenseRed
import com.usj.fintrack.presentation.theme.IncomeGreen
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BudgetItem(
    budget: Budget,
    budgetStatus: BudgetStatus?,
    categoryName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val sym = LocalCurrencySymbol.current
    val statusColor = when (budgetStatus?.computedStatusType ?: budget.status) {
        BudgetStatusType.ON_TRACK -> IncomeGreen
        BudgetStatusType.WARNING  -> Amber40
        BudgetStatusType.EXCEEDED -> ExpenseRed
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ── Header row: category name + status badge ─────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${formatter.format(Date(budget.periodStartDate))} – ${formatter.format(Date(budget.periodEndDate))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SuggestionChip(
                    onClick = {},
                    label = {
                        Text(
                            text = (budgetStatus?.computedStatusType ?: budget.status).name.replace('_', ' '),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        labelColor = statusColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Progress bar ─────────────────────────────────────────────────
            BudgetProgressBar(budget = budget, budgetStatus = budgetStatus)

            // ── Remaining amount ─────────────────────────────────────────────
            if (budgetStatus != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (budgetStatus.remainingAmount >= 0)
                        "$sym${"%.2f".format(budgetStatus.remainingAmount)} remaining"
                    else
                        "$sym${"%.2f".format(-budgetStatus.remainingAmount)} over budget",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor
                )
            }

            // ── Edit / Delete actions ─────────────────────────────────────────
            if (onEdit != null || onDelete != null) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    onEdit?.let {
                        IconButton(onClick = it) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit budget")
                        }
                    }
                    onDelete?.let {
                        IconButton(onClick = it) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete budget",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
