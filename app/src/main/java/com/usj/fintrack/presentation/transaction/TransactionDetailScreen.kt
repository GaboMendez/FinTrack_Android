package com.usj.fintrack.presentation.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.usj.fintrack.domain.model.enum.TransactionType
import com.usj.fintrack.presentation.component.CategoryChip
import com.usj.fintrack.presentation.component.LoadingIndicator
import com.usj.fintrack.presentation.theme.ExpenseRed
import com.usj.fintrack.presentation.theme.IncomeGreen
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionDetailScreen(
    transactionId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    viewModel: TransactionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }

    LaunchedEffect(transactionId) {
        viewModel.loadTransaction(transactionId)
    }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onNavigateBack()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissError()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This will reverse the account balance.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteTransaction()
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
                title = { Text("Transaction Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.transaction != null) {
                        IconButton(onClick = { onNavigateToEdit(uiState.transaction!!.id) }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Transaction"
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Transaction",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.transaction == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Transaction not found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            else -> {
                val transaction = uiState.transaction!!
                val amountColor = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed
                val sign = if (transaction.type == TransactionType.INCOME) "+" else "-"
                val sym = LocalCurrencySymbol.current

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Amount
                    Text(
                        text = "$sign$sym${"%.2f".format(transaction.amount)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = amountColor
                    )

                    // Type badge
                    Text(
                        text = transaction.type.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = amountColor
                    )

                    // Account
                    uiState.accountName?.let {
                        LabeledField(label = "Account", value = it)
                    }

                    // Description
                    LabeledField(label = "Description", value = transaction.description)

                    // Date
                    LabeledField(
                        label = "Date",
                        value = dateFormatter.format(Date(transaction.date))
                    )

                    // Location
                    transaction.location?.let {
                        LabeledField(label = "Location", value = it)
                    }

                    // Categories
                    if (transaction.categories.isNotEmpty()) {
                        Text("Categories", style = MaterialTheme.typography.labelLarge)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            transaction.categories.forEach { category ->
                                CategoryChip(
                                    category = category,
                                    selected = true,
                                    onClick = {}
                                )
                            }
                        }
                    }

                    // Image URI (if present)
                    transaction.imageUri?.let {
                        LabeledField(label = "Receipt", value = it)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun LabeledField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
