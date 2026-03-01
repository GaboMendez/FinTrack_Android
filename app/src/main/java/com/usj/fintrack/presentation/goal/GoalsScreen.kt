package com.usj.fintrack.presentation.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.presentation.navigation.Screen
import com.usj.fintrack.presentation.component.LoadingIndicator
import com.usj.fintrack.presentation.goal.component.GoalProgressCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var goalToDelete by remember { mutableStateOf<Goal?>(null) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissError()
        }
    }

    // Delete confirmation dialog
    goalToDelete?.let { goal ->
        AlertDialog(
            onDismissRequest = { goalToDelete = null },
            title = { Text("Delete Goal") },
            text = { Text("Delete \"${goal.name}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteGoal(goal.id)
                        goalToDelete = null
                    }
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { goalToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Goals") },
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
                Icon(Icons.Default.Add, contentDescription = "Add goal")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { LoadingIndicator() }
            return@Scaffold
        }

        if (uiState.goals.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No goals yet.\nTap + to create your first goal.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.goals, key = { it.id }) { goal ->
                GoalProgressCard(
                    goal = goal,
                    onClick = { onNavigateToDetail(goal.id) }
                )
            }
        }
    }
}
