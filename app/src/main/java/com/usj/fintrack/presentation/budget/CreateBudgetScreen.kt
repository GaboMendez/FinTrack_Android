package com.usj.fintrack.presentation.budget

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.usj.fintrack.presentation.component.AmountTextField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBudgetScreen(
    onNavigateBack: () -> Unit,
    budgetId: Long? = null,
    viewModel: BudgetsViewModel = hiltViewModel()
) {
    val isEditMode = budgetId != null
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Default period = current calendar month
    val defaultStart = remember {
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    val defaultEnd = remember {
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var limitAmount by rememberSaveable { mutableStateOf("") }
    var periodStartDate by remember { mutableLongStateOf(defaultStart) }
    var periodEndDate by remember { mutableLongStateOf(defaultEnd) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var fieldsInitialized by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    val limitError = limitAmount.toDoubleOrNull()?.let { it <= 0 } ?: (limitAmount.isNotEmpty())
    val canSave = selectedCategoryId != null &&
            limitAmount.toDoubleOrNull() != null &&
            limitAmount.toDoubleOrNull()!! > 0 &&
            periodEndDate > periodStartDate

    // ── Edit mode: load budget then populate fields ──────────────────────────
    LaunchedEffect(budgetId) {
        if (budgetId != null) viewModel.loadBudgetForEditing(budgetId)
    }
    LaunchedEffect(uiState.editingBudget) {
        if (!fieldsInitialized && uiState.editingBudget != null) {
            selectedCategoryId = uiState.editingBudget!!.categoryId
            limitAmount = "%.2f".format(uiState.editingBudget!!.limitAmount)
            periodStartDate = uiState.editingBudget!!.periodStartDate
            periodEndDate = uiState.editingBudget!!.periodEndDate
            fieldsInitialized = true
        }
    }

    // ── Navigation after success ─────────────────────────────────────────────
    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) { viewModel.clearCreateSuccess(); onNavigateBack() }
    }
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) { viewModel.clearUpdateSuccess(); onNavigateBack() }
    }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { snackbarHostState.showSnackbar(it); viewModel.dismissError() }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.clearEditingBudget() }
    }

    // ── Date picker dialogs ──────────────────────────────────────────────────
    if (showStartDatePicker) {
        val dpState = rememberDatePickerState(initialSelectedDateMillis = periodStartDate)
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dpState.selectedDateMillis?.let { periodStartDate = it }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = dpState) }
    }

    if (showEndDatePicker) {
        val dpState = rememberDatePickerState(initialSelectedDateMillis = periodEndDate)
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dpState.selectedDateMillis?.let { periodEndDate = it }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = dpState) }
    }

    // ── Screen ───────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Budget" else "New Budget") },
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
            // ── Category dropdown ────────────────────────────────────────────
            Text("Category", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))

            if (uiState.categories.isEmpty()) {
                Text(
                    text = "No categories available. Add transactions with categories first.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                val selectedCategoryName = uiState.categories
                    .find { it.id == selectedCategoryId }?.name ?: "Select a category"

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        uiState.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Limit amount ─────────────────────────────────────────────────
            AmountTextField(
                value = limitAmount,
                onValueChange = { limitAmount = it },
                label = "Limit Amount",
                isError = limitAmount.isNotEmpty() && limitError,
                modifier = Modifier.fillMaxWidth()
            )
            if (limitAmount.isNotEmpty() && limitError) {
                Text(
                    text = "Amount must be greater than 0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Period start date ────────────────────────────────────────────
            Text("Period Start", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = { showStartDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(dateFormatter.format(Date(periodStartDate)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Period end date ──────────────────────────────────────────────
            Text("Period End", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = { showEndDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(dateFormatter.format(Date(periodEndDate)))
            }
            if (periodEndDate <= periodStartDate) {
                Text(
                    text = "End date must be after start date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Save button ──────────────────────────────────────────────────
            Button(
                onClick = {
                    val limit = limitAmount.toDoubleOrNull() ?: return@Button
                    val catId = selectedCategoryId ?: return@Button
                    if (isEditMode) {
                        viewModel.updateBudget(catId, limit, periodStartDate, periodEndDate)
                    } else {
                        viewModel.createBudget(catId, limit, periodStartDate, periodEndDate)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canSave
            ) {
                Text(if (isEditMode) "Update Budget" else "Create Budget")
            }
        }
    }
}
