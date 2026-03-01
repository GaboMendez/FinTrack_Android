package com.usj.fintrack.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.usj.fintrack.presentation.component.LoadingIndicator
import com.usj.fintrack.presentation.dashboard.component.BalanceSummaryCard
import com.usj.fintrack.presentation.dashboard.component.BudgetOverview
import com.usj.fintrack.presentation.dashboard.component.RecentTransactionsList
import com.usj.fintrack.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FinTrack") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateTransaction.route) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add transaction")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingIndicator(
                message = "Loading...",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                BalanceSummaryCard(
                    totalBalance = uiState.totalBalance,
                    monthlyIncome = uiState.monthlyIncome,
                    monthlyExpense = uiState.monthlyExpense,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.budgets.isNotEmpty()) {
                    Text(
                        text = "Budgets",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BudgetOverview(
                        budgets = uiState.budgets,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    RecentTransactionsList(
                        transactions = uiState.recentTransactions,
                        onTransactionClick = { id ->
                            navController.navigate(Screen.TransactionDetail.navRoute(id))
                        }
                    )
                }
            }
        }
    }
}
