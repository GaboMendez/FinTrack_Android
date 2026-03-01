package com.usj.fintrack.presentation.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.presentation.component.LoadingIndicator
import com.usj.fintrack.presentation.transaction.component.TransactionFilter
import com.usj.fintrack.presentation.transaction.component.TransactionsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissError()
        }
    }

    transactionToDelete?.let { tx ->
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Transaction") },
            text = { Text("Delete \"${tx.description}\"? This will reverse the account balance.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTransaction(tx.id)
                        transactionToDelete = null
                    }
                ) { Text("Delete", color = androidx.compose.material3.MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") },
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
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (uiState.isLoading) {
            LoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                TransactionFilter(
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = viewModel::setFilter
                )
                TransactionsList(
                    transactions = uiState.filteredTransactions,
                    onTransactionClick = onNavigateToDetail,
                    accounts = uiState.accounts,
                    onTransactionEdit = onNavigateToEdit,
                    onTransactionDelete = { id ->
                        transactionToDelete = uiState.transactions.find { it.id == id }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
