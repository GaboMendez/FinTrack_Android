package com.usj.fintrack.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Dashboard",
        icon = Icons.Default.Dashboard,
        route = Screen.Dashboard.route
    ),
    BottomNavItem(
        label = "Gastos",
        icon = Icons.Default.Receipt,
        route = Screen.Transactions.route
    ),
    BottomNavItem(
        label = "Cuentas",
        icon = Icons.Default.AccountBalance,
        route = Screen.Accounts.route
    ),
    BottomNavItem(
        label = "Presupuestos",
        icon = Icons.Default.PieChart,
        route = Screen.Budgets.route
    ),
    BottomNavItem(
        label = "Metas",
        icon = Icons.Default.Flag,
        route = Screen.Goals.route
    )
)
