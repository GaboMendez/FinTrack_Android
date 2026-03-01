package com.usj.fintrack.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.usj.fintrack.domain.model.AppTheme
import com.usj.fintrack.presentation.navigation.AppNavGraph
import com.usj.fintrack.presentation.navigation.Screen
import com.usj.fintrack.presentation.navigation.bottomNavItems
import com.usj.fintrack.presentation.navigation.mainScreenRoutes
import com.usj.fintrack.presentation.settings.SettingsViewModel
import com.usj.fintrack.presentation.theme.FinTrackTheme
import com.usj.fintrack.presentation.theme.LocalCurrencySymbol
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (settingsState.preferences.theme) {
                AppTheme.LIGHT  -> false
                AppTheme.DARK   -> true
                AppTheme.SYSTEM -> systemDark
            }

            FinTrackTheme(darkTheme = darkTheme) {
                CompositionLocalProvider(
                    LocalCurrencySymbol provides settingsState.preferences.currency.symbol
                ) {
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
                                        label = { Text(text = item.label, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController     = navController,
                        startDestination  = Screen.Dashboard.route,
                        modifier          = Modifier.padding(innerPadding)
                    )
                }
                } // end CompositionLocalProvider
            }
        }
    }
}