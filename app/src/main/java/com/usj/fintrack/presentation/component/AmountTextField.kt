package com.usj.fintrack.presentation.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol

@Composable
fun AmountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    currencySymbol: String? = null,
    label: String = "Amount",
    isError: Boolean = false
) {
    val sym = currencySymbol ?: LocalCurrencySymbol.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        leadingIcon = {
            Text(
                text = sym,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        isError = isError,
        singleLine = true,
        supportingText = if (isError) {
            { Text("Enter a valid positive amount") }
        } else {
            null
        }
    )
}
