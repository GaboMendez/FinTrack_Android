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
import com.usj.fintrack.domain.model.enum.BudgetStatusType
import com.usj.fintrack.presentation.theme.Amber40
import com.usj.fintrack.presentation.theme.ExpenseRed
import com.usj.fintrack.presentation.theme.IncomeGreen

@Composable
fun BudgetProgressBar(
    budget: Budget,
    modifier: Modifier = Modifier
) {
    val progress = if (budget.limitAmount > 0) {
        (budget.spentAmount / budget.limitAmount).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    val progressColor: Color = when (budget.status) {
        BudgetStatusType.ON_TRACK -> IncomeGreen
        BudgetStatusType.WARNING -> Amber40
        BudgetStatusType.EXCEEDED -> ExpenseRed
    }

    Column(modifier = modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "€%.2f spent".format(budget.spentAmount),
                style = MaterialTheme.typography.bodySmall,
                color = progressColor
            )
            Text(
                text = "of €%.2f".format(budget.limitAmount),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
