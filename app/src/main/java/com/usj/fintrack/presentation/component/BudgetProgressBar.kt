package com.usj.fintrack.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.BudgetStatus
import com.usj.fintrack.domain.model.enum.BudgetStatusType
import com.usj.fintrack.presentation.theme.Amber40
import com.usj.fintrack.presentation.theme.ExpenseRed
import com.usj.fintrack.presentation.theme.IncomeGreen
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol

/**
 * Renders a spending progress bar for a budget.
 *
 * When [budgetStatus] is provided, the computed spend / status from real
 * transactions is used.  Falls back to the stored [Budget.spentAmount]
 * when [budgetStatus] is null (e.g. previews).
 */
@Composable
fun BudgetProgressBar(
    budget: Budget,
    budgetStatus: BudgetStatus? = null,
    modifier: Modifier = Modifier
) {
    val sym = LocalCurrencySymbol.current
    val spentAmount = budgetStatus?.computedSpentAmount ?: budget.spentAmount
    val statusType  = budgetStatus?.computedStatusType  ?: budget.status

    val progress = if (budget.limitAmount > 0) {
        (spentAmount / budget.limitAmount).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    val progressColor: Color = when (statusType) {
        BudgetStatusType.ON_TRACK -> IncomeGreen
        BudgetStatusType.WARNING  -> Amber40
        BudgetStatusType.EXCEEDED -> ExpenseRed
    }

    Column(modifier = modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$sym${"%.2f".format(spentAmount)} spent",
                style = MaterialTheme.typography.bodySmall,
                color = progressColor
            )
            Text(
                text = "of $sym${"%.2f".format(budget.limitAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}