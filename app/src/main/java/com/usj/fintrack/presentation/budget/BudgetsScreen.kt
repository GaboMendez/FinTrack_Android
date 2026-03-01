package com.usj.fintrack.presentation.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.navigation.NavController
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.presentation.budget.component.BudgetItem
import com.usj.fintrack.presentation.component.LoadingIndicator
import com.usj.fintrack.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetsScreen(
    navController: NavController,
    onNavigateToSettings: () -> Unit,
    viewModel: BudgetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var budgetToDelete by remember { mutableStateOf<Budget?>(null) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissError()
        }
    }

    // ── Delete confirmation dialog ────────────────────────────────────────────
    if (budgetToDelete != null) {
        val categoryName = uiState.categories
            .find { it.id == budgetToDelete!!.categoryId }?.name ?: "this budget"
        AlertDialog(
            onDismissRequest = { budgetToDelete = null },
            title = { Text("Delete Budget") },
            text = { Text("Are you sure you want to delete the budget for \"$categoryName\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        budgetToDelete?.let { viewModel.deleteBudget(it.id) }
                        budgetToDelete = null
                    }
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { budgetToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budgets") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.Categories.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Manage categories"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateBudget.route) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add budget")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingIndicator(
                    message = "Loading budgets...",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            uiState.budgets.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No budgets yet.\nTap + to create your first budget.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.budgets, key = { it.id }) { budget ->
                        BudgetItem(
                            budget = budget,
                            budgetStatus = uiState.budgetStatuses[budget.id],
                            categoryName = uiState.categories
                                .find { it.id == budget.categoryId }?.name
                                ?: "Category #${budget.categoryId}",
                            onClick = {
                                navController.navigate(Screen.BudgetDetail.navRoute(budget.id))
                            },
                            onEdit = {
                                navController.navigate(Screen.EditBudget.navRoute(budget.id))
                            },
                            onDelete = { budgetToDelete = budget }
                        )
                    }
                }
            }
        }
    }
}
