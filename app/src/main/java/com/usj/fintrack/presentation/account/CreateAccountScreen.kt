package com.usj.fintrack.presentation.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.usj.fintrack.domain.model.enum.AccountType
import com.usj.fintrack.domain.model.enum.Currency
import com.usj.fintrack.presentation.component.AmountTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    onNavigateBack: () -> Unit,
    accountId: Long? = null,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val isEditMode = accountId != null
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var name by rememberSaveable { mutableStateOf("") }
    var selectedType by rememberSaveable { mutableStateOf(AccountType.CHECKING) }
    var initialBalance by rememberSaveable { mutableStateOf("0.00") }
    var selectedCurrency by rememberSaveable { mutableStateOf(Currency.EUR) }

    var typeExpanded by remember { mutableStateOf(false) }
    var currencyExpanded by remember { mutableStateOf(false) }

    // Track whether we have already populated the fields from the loaded account.
    var fieldsInitialized by remember { mutableStateOf(false) }

    val nameError = name.isBlank()
    val balanceError = initialBalance.toDoubleOrNull() == null || initialBalance.toDoubleOrNull()!! < 0

    // In edit mode, load the existing account into the ViewModel state.
    LaunchedEffect(accountId) {
        if (accountId != null) viewModel.loadAccountForEditing(accountId)
    }

    // Pre-populate fields once `editingAccount` is available (edit mode only).
    LaunchedEffect(uiState.editingAccount) {
        if (!fieldsInitialized && uiState.editingAccount != null) {
            name = uiState.editingAccount!!.name
            selectedType = uiState.editingAccount!!.type
            initialBalance = "%.2f".format(uiState.editingAccount!!.balance)
            selectedCurrency = uiState.editingAccount!!.currency
            fieldsInitialized = true
        }
    }

    // Navigate back after a successful create.
    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            viewModel.clearCreateSuccess()
            onNavigateBack()
        }
    }

    // Navigate back after a successful update.
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            viewModel.clearUpdateSuccess()
            onNavigateBack()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissError()
        }
    }

    // Clean up the editingAccount state when leaving this screen.
    DisposableEffect(Unit) {
        onDispose { viewModel.clearEditingAccount() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Account" else "New Account") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Account Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = name.isNotEmpty() && nameError,
                supportingText = if (name.isNotEmpty() && nameError) {
                    { Text("Name must not be blank") }
                } else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Account type dropdown
            Text("Account Type", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = it }
            ) {
                OutlinedTextField(
                    value = accountTypeLabel(selectedType),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    AccountType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(accountTypeLabel(type)) },
                            onClick = {
                                selectedType = type
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Balance field (label changes for edit mode)
            AmountTextField(
                value = initialBalance,
                onValueChange = { initialBalance = it },
                currencySymbol = selectedCurrency.symbol,
                label = if (isEditMode) "Balance" else "Initial Balance",
                isError = initialBalance.isNotEmpty() && balanceError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Currency dropdown
            Text("Currency", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = it }
            ) {
                OutlinedTextField(
                    value = "${selectedCurrency.name} (${selectedCurrency.symbol})",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = currencyExpanded,
                    onDismissRequest = { currencyExpanded = false }
                ) {
                    Currency.entries.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text("${currency.name} (${currency.symbol})") },
                            onClick = {
                                selectedCurrency = currency
                                currencyExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isEditMode) {
                        viewModel.updateAccount(
                            id = accountId!!,
                            name = name.trim(),
                            type = selectedType,
                            balance = initialBalance.toDoubleOrNull() ?: 0.0,
                            currency = selectedCurrency
                        )
                    } else {
                        viewModel.createAccount(
                            name = name.trim(),
                            type = selectedType,
                            initialBalance = initialBalance.toDoubleOrNull() ?: 0.0,
                            currency = selectedCurrency
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !nameError && !balanceError && !uiState.isLoading
            ) {
                Text(if (isEditMode) "Update Account" else "Save Account")
            }
        }
    }
}

private fun accountTypeLabel(type: AccountType) = when (type) {
    AccountType.CHECKING    -> "Checking"
    AccountType.SAVINGS     -> "Savings"
    AccountType.CASH        -> "Cash"
    AccountType.CREDIT_CARD -> "Credit Card"
}
