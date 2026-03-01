package com.usj.fintrack.presentation.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.BudgetStatus
import com.usj.fintrack.domain.model.enum.BudgetStatusType
import com.usj.fintrack.presentation.component.BudgetProgressBar
import com.usj.fintrack.presentation.component.LoadingIndicator
import com.usj.fintrack.presentation.component.TransactionListItem
import com.usj.fintrack.presentation.theme.Amber40
import com.usj.fintrack.presentation.theme.ExpenseRed
import com.usj.fintrack.presentation.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailScreen(
    budgetId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit,
    viewModel: BudgetDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(budgetId) {
        viewModel.loadBudget(budgetId)
    }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) onNavigateBack()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissError()
        }
    }

    // ── Delete confirmation dialog ────────────────────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Budget") },
            text = {
                Text(
                    "Are you sure you want to delete the budget for " +
                    "\"${uiState.categoryName ?: "this category"}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteBudget(budgetId)
                    }
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.categoryName ?: "Budget Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(budgetId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit budget")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete budget",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                // ── Budget summary card ──────────────────────────────────────
                item {
                    uiState.budget?.let { budget ->
                        BudgetSummaryCard(
                            budget = budget,
                            categoryName = uiState.categoryName,
                            budgetStatus = uiState.budgetStatus
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Transactions in this category",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // ── Related transactions ─────────────────────────────────────
                if (uiState.transactions.isEmpty()) {
                    item {
                        Text(
                            text = "No transactions in this category yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(uiState.transactions, key = { it.id }) { transaction ->
                        TransactionListItem(
                            transaction = transaction,
                            onClick = { onNavigateToTransactionDetail(transaction.id) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun BudgetSummaryCard(
    budget: Budget,
    categoryName: String?,
    budgetStatus: BudgetStatus?
) {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val statusColor = when (budgetStatus?.computedStatusType ?: budget.status) {
        BudgetStatusType.ON_TRACK -> IncomeGreen
        BudgetStatusType.WARNING  -> Amber40
        BudgetStatusType.EXCEEDED -> ExpenseRed
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Category name
            if (categoryName != null) {
                BudgetInfoRow(label = "Category", value = categoryName)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Period
            BudgetInfoRow(
                label = "Period",
                value = "${formatter.format(Date(budget.periodStartDate))} – ${formatter.format(Date(budget.periodEndDate))}"
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Status
            BudgetInfoRow(
                label = "Status",
                value = budget.status.name.replace('_', ' ')
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            BudgetProgressBar(budget = budget, budgetStatus = budgetStatus)

            Spacer(modifier = Modifier.height(8.dp))

            // Remaining
            if (budgetStatus != null) {
                val remaining = budgetStatus.remainingAmount
                Text(
                    text = if (remaining >= 0)
                        "\u20ac%.2f remaining of \u20ac%.2f".format(remaining, budget.limitAmount)
                    else
                        "\u20ac%.2f over budget (limit: \u20ac%.2f)".format(-remaining, budget.limitAmount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor
                )
            }
        }
    }
}

@Composable
private fun BudgetInfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
