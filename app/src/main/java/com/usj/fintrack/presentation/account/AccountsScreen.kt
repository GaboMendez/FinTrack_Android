package com.usj.fintrack.presentation.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.presentation.account.component.AccountCard
import com.usj.fintrack.presentation.component.LoadingIndicator
import com.usj.fintrack.presentation.navigation.Screen
import com.usj.fintrack.presentation.theme.IncomeGreen
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    navController: NavController,
    onNavigateToSettings: () -> Unit,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sym = LocalCurrencySymbol.current
    val snackbarHostState = remember { SnackbarHostState() }
    var accountToDelete by remember { mutableStateOf<Account?>(null) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissError()
        }
    }

    if (accountToDelete != null) {
        AlertDialog(
            onDismissRequest = { accountToDelete = null },
            title = { Text("Delete Account") },
            text = {
                Text(
                    "Are you sure you want to delete \"${accountToDelete?.name}\"? " +
                    "All associated transactions will be removed."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        accountToDelete?.let { viewModel.deleteAccount(it.id) }
                        accountToDelete = null
                    }
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { accountToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Accounts")
                        Text(
                            text = "Total: $sym%.2f".format(uiState.totalBalance),
                            style = MaterialTheme.typography.bodySmall,
                            color = IncomeGreen
                        )
                    }
                },
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
                onClick = { navController.navigate(Screen.CreateAccount.route) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add account")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingIndicator(
                message = "Loading accounts...",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else if (uiState.accounts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No accounts yet.\nTap + to add your first account.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.accounts, key = { it.id }) { account ->
                    AccountCard(
                        account = account,
                        onClick = {
                            navController.navigate(Screen.AccountDetail.navRoute(account.id))
                        },
                        onEdit = {
                            navController.navigate(Screen.EditAccount.navRoute(account.id))
                        },
                        onDelete = {
                            accountToDelete = account
                        }
                    )
                }
            }
        }
    }
}
