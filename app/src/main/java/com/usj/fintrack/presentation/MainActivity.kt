package com.usj.fintrack.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.usj.fintrack.presentation.navigation.Screen
import com.usj.fintrack.presentation.navigation.bottomNavItems
import com.usj.fintrack.presentation.navigation.mainScreenRoutes
import com.usj.fintrack.ui.theme.FinTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinTrackTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (currentRoute in mainScreenRoutes) {
                            NavigationBar {
                                bottomNavItems.forEach { item ->
                                    NavigationBarItem(
                                        selected = currentRoute == item.route,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.label
                                            )
                                        },
                                        label = { Text(text = item.label) }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Dashboard.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Auth flow — placeholder until Fase 5
                        composable(Screen.Splash.route) {
                            PlaceholderScreen("Splash")
                        }
                        composable(Screen.Onboarding.route) {
                            PlaceholderScreen("Onboarding")
                        }
                        composable(Screen.ProfileSetup.route) {
                            PlaceholderScreen("Configurar perfil")
                        }

                        // Main destinations — placeholder until Fase 5
                        composable(Screen.Dashboard.route) {
                            PlaceholderScreen("Dashboard")
                        }
                        composable(Screen.Transactions.route) {
                            PlaceholderScreen("Transacciones")
                        }
                        composable(Screen.Accounts.route) {
                            PlaceholderScreen("Cuentas")
                        }
                        composable(Screen.Budgets.route) {
                            PlaceholderScreen("Presupuestos")
                        }
                        composable(Screen.Goals.route) {
                            PlaceholderScreen("Metas")
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

