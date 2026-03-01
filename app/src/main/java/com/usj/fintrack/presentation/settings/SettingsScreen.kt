package com.usj.fintrack.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.usj.fintrack.domain.model.AppTheme
import com.usj.fintrack.domain.model.enum.Currency
import com.usj.fintrack.presentation.component.LoadingIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ── User info card ───────────────────────────────────────────────
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        if (uiState.user != null) {
                            Text(
                                text = uiState.user!!.email.ifBlank { "No email" },
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Member since ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(
                                    uiState.user!!.createdAt))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        } else {
                            Text(
                                text = "Not signed in",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.height(2.dp))
                        }
                    }
                }
            }

            // ── Appearance ───────────────────────────────────────────────────
            SettingsSectionHeader(title = "Appearance")

            SettingsOptionRow(
                label = "Light",
                selected = uiState.preferences.theme == AppTheme.LIGHT,
                onClick = { viewModel.updateTheme(AppTheme.LIGHT) }
            )
            SettingsOptionRow(
                label = "Dark",
                selected = uiState.preferences.theme == AppTheme.DARK,
                onClick = { viewModel.updateTheme(AppTheme.DARK) }
            )
            SettingsOptionRow(
                label = "System default",
                selected = uiState.preferences.theme == AppTheme.SYSTEM,
                onClick = { viewModel.updateTheme(AppTheme.SYSTEM) }
            )

            Spacer(Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ── Currency ─────────────────────────────────────────────────────
            SettingsSectionHeader(title = "Currency")

            Currency.entries.forEach { currency ->
                SettingsOptionRow(
                    label = "${currency.name} – ${currency.symbol}",
                    selected = uiState.preferences.currency == currency,
                    onClick = { viewModel.updateCurrency(currency) }
                )
            }

            Spacer(Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ── About ────────────────────────────────────────────────────────
            SettingsSectionHeader(title = "About")
            Text(
                text = "FinTrack v1.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Text(
                text = "Personal finance management",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ─── Private helpers ──────────────────────────────────────────────────────────

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
