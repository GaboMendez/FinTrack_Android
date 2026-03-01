package com.usj.fintrack.presentation.goal

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalScreen(
    onNavigateBack: () -> Unit,
    goalId: Long? = null,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val isEditMode = goalId != null
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Default deadline = 30 days from now in UTC
    val defaultDeadline = remember {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        cal.add(Calendar.DAY_OF_MONTH, 30)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        cal.timeInMillis
    }

    var goalName by rememberSaveable { mutableStateOf("") }
    var targetAmount by rememberSaveable { mutableStateOf("") }
    var deadline by remember { mutableLongStateOf(defaultDeadline) }
    var showDeadlinePicker by rememberSaveable { mutableStateOf(false) }

    // Load editing goal once when entering edit mode
    DisposableEffect(Unit) {
        if (isEditMode) viewModel.loadGoalForEditing(goalId!!)
        onDispose { viewModel.clearEditingGoal() }
    }

    // Pre-populate fields when editing goal is loaded
    LaunchedEffect(uiState.editingGoal) {
        uiState.editingGoal?.let { goal ->
            if (isEditMode) {
                goalName = goal.name
                targetAmount = goal.targetAmount.toString()
                deadline = goal.deadline
            }
        }
    }

    // Show errors
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissError()
        }
    }

    // Navigate back on success
    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            viewModel.resetCreateSuccess()
            onNavigateBack()
        }
    }
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            viewModel.resetUpdateSuccess()
            onNavigateBack()
        }
    }

    // Deadline DatePicker dialog
    if (showDeadlinePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = deadline)
        DatePickerDialog(
            onDismissRequest = { showDeadlinePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { ms ->
                            // Use end-of-day UTC to cover the full selected date
                            deadline = ms + 86_399_999L
                        }
                        showDeadlinePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDeadlinePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val deadlineFormatted = remember(deadline) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(deadline))
    }

    val isSaveEnabled = goalName.isNotBlank() &&
            targetAmount.toDoubleOrNull()?.let { it > 0 } == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Goal" else "New Goal") },
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // ── Name ─────────────────────────────────────────────────────────
            OutlinedTextField(
                value = goalName,
                onValueChange = { goalName = it },
                label = { Text("Goal name") },
                placeholder = { Text("e.g. Vacation fund") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Target Amount ─────────────────────────────────────────────────
            AmountTextField(
                value = targetAmount,
                onValueChange = { targetAmount = it },
                label = "Target amount",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Deadline ──────────────────────────────────────────────────────
            Text(
                text = "Deadline",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = { showDeadlinePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(deadlineFormatted)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Save button ──────────────────────────────────────────────────
            Button(
                onClick = {
                    val amount = targetAmount.toDoubleOrNull() ?: return@Button
                    if (isEditMode) {
                        viewModel.updateGoal(goalName, amount, deadline)
                    } else {
                        viewModel.createGoal(goalName, amount, deadline)
                    }
                },
                enabled = isSaveEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Save Changes" else "Create Goal")
            }
        }
    }
}
