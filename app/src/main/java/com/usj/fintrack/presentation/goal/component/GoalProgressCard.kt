package com.usj.fintrack.presentation.goal.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.presentation.theme.Amber40
import com.usj.fintrack.presentation.theme.FinTrackGreen40
import com.usj.fintrack.presentation.theme.FinTrackGreen80
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Card summarising a single savings goal.
 *
 * Shows: goal name, linear progress bar (currentAmount / targetAmount),
 * deadline, remaining amount and a "Completed" badge when done.
 */
@Composable
fun GoalProgressCard(
    goal: Goal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (goal.targetAmount > 0) {
        (goal.currentAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    // Treat as complete either when the model flag is set OR progress reached 100 %
    val isEffectivelyCompleted = goal.isCompleted || progress >= 1f
    val progressColor = if (isEffectivelyCompleted) FinTrackGreen40 else Amber40
    val deadlineFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(goal.deadline))
    val remaining = (goal.targetAmount - goal.currentAmount).coerceAtLeast(0.0)
    val sym = LocalCurrencySymbol.current

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ── Header row: name + completed badge ───────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                if (isEffectivelyCompleted) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = FinTrackGreen80.copy(alpha = 0.20f)
                    ) {
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.labelSmall,
                            color = FinTrackGreen40,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Progress bar ─────────────────────────────────────────────────
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ── Amounts row ──────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$sym${"%.2f".format(goal.currentAmount)} saved",
                    style = MaterialTheme.typography.bodySmall,
                    color = progressColor
                )
                Text(
                    text = "of $sym${"%.2f".format(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ── Deadline + remaining ─────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Deadline: $deadlineFormatted",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!isEffectivelyCompleted) {
                    Text(
                        text = "$sym${"%.2f".format(remaining)} left",
                        style = MaterialTheme.typography.bodySmall,
                        color = Amber40
                    )
                }
            }
        }
    }
}
