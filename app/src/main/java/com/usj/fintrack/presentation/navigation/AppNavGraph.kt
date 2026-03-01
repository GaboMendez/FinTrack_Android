package com.usj.fintrack.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.usj.fintrack.presentation.account.AccountDetailScreen
import com.usj.fintrack.presentation.account.AccountsScreen
import com.usj.fintrack.presentation.account.CreateAccountScreen
import com.usj.fintrack.presentation.budget.BudgetDetailScreen
import com.usj.fintrack.presentation.budget.BudgetsScreen
import com.usj.fintrack.presentation.budget.CreateBudgetScreen
import com.usj.fintrack.presentation.category.CategoriesScreen
import com.usj.fintrack.presentation.dashboard.DashboardScreen
import com.usj.fintrack.presentation.goal.CreateGoalScreen
import com.usj.fintrack.presentation.goal.GoalDetailScreen
import com.usj.fintrack.presentation.goal.GoalsScreen
import com.usj.fintrack.presentation.transaction.CreateTransactionScreen
import com.usj.fintrack.presentation.transaction.TransactionDetailScreen
import com.usj.fintrack.presentation.transaction.TransactionsScreen

/**
 * Central navigation graph for FinTrack.
 *
 * All routes are registered here. While real screens are being built (Fase 5),
 * each destination renders a [PlaceholderScreen]. Replace each lambda with the
 * actual screen composable as it becomes available.
 *
 * @param navController  The [NavHostController] shared with [MainActivity].
 * @param startDestination  Start destination route; defaults to [Screen.Dashboard].
 * @param modifier  Applied to the [NavHost].
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Dashboard.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController    = navController,
        startDestination = startDestination,
        modifier         = modifier
    ) {

        // ── Auth flow ────────────────────────────────────────────────────────
        composable(Screen.Splash.route) {
            PlaceholderScreen("Splash")
        }
        composable(Screen.Onboarding.route) {
            PlaceholderScreen("Onboarding")
        }
        composable(Screen.ProfileSetup.route) {
            PlaceholderScreen("Configurar perfil")
        }

        // ── Main bottom-nav destinations ─────────────────────────────────────
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.Transactions.route) {
            TransactionsScreen(
                onNavigateToCreate = { navController.navigate(Screen.CreateTransaction.route) },
                onNavigateToDetail = { id -> navController.navigate(Screen.TransactionDetail.navRoute(id)) },
                onNavigateToEdit = { id -> navController.navigate(Screen.EditTransaction.navRoute(id)) }
            )
        }
        composable(Screen.Accounts.route) {
            AccountsScreen(navController = navController)
        }
        composable(Screen.Budgets.route) {
            BudgetsScreen(navController = navController)
        }
        composable(Screen.Goals.route) {
            GoalsScreen(
                onNavigateToCreate = { navController.navigate(Screen.CreateGoal.route) },
                onNavigateToDetail = { id -> navController.navigate(Screen.GoalDetail.navRoute(id)) },
                onNavigateToEdit   = { id -> navController.navigate(Screen.EditGoal.navRoute(id)) }
            )
        }

        // ── Transaction sub-screens ──────────────────────────────────────────
        composable(Screen.CreateTransaction.route) {
            CreateTransactionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route     = Screen.EditTransaction.routeWithArg,
            arguments = listOf(
                navArgument(Screen.EditTransaction.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            CreateTransactionScreen(
                onNavigateBack = { navController.popBackStack() },
                transactionId = backStackEntry.arguments?.getLong(Screen.EditTransaction.ARG_ID)
            )
        }

        composable(
            route     = Screen.TransactionDetail.routeWithArg,
            arguments = listOf(
                navArgument(Screen.TransactionDetail.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Screen.TransactionDetail.ARG_ID) ?: 0L
            TransactionDetailScreen(
                transactionId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate(Screen.EditTransaction.navRoute(id)) }
            )
        }

        composable(Screen.TicketCapture.route) {
            PlaceholderScreen("Capturar ticket")
        }

        composable(
            route     = Screen.TransactionReview.routeWithArg,
            arguments = listOf(
                navArgument(Screen.TransactionReview.ARG_IMAGE_URI) {
                    type         = NavType.StringType
                    nullable     = true
                    defaultValue = null
                }
            )
        ) {
            PlaceholderScreen("Revisar ticket")
        }

        // ── Account sub-screens ──────────────────────────────────────────────
        composable(Screen.CreateAccount.route) {
            CreateAccountScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route     = Screen.EditAccount.routeWithArg,
            arguments = listOf(
                navArgument(Screen.EditAccount.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            CreateAccountScreen(
                onNavigateBack = { navController.popBackStack() },
                accountId = backStackEntry.arguments?.getLong(Screen.EditAccount.ARG_ID)
            )
        }

        composable(
            route     = Screen.AccountDetail.routeWithArg,
            arguments = listOf(
                navArgument(Screen.AccountDetail.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Screen.AccountDetail.ARG_ID) ?: 0L
            AccountDetailScreen(
                accountId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Screen.EditAccount.navRoute(id)) },
                onNavigateToTransactionDetail = { id -> navController.navigate(Screen.TransactionDetail.navRoute(id)) }
            )
        }

        // ── Budget sub-screens ───────────────────────────────────────────────
        composable(Screen.Categories.route) {
            CategoriesScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.CreateBudget.route) {
            CreateBudgetScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route     = Screen.EditBudget.routeWithArg,
            arguments = listOf(
                navArgument(Screen.EditBudget.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            CreateBudgetScreen(
                onNavigateBack = { navController.popBackStack() },
                budgetId = backStackEntry.arguments?.getLong(Screen.EditBudget.ARG_ID)
            )
        }

        composable(
            route     = Screen.BudgetDetail.routeWithArg,
            arguments = listOf(
                navArgument(Screen.BudgetDetail.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Screen.BudgetDetail.ARG_ID) ?: 0L
            BudgetDetailScreen(
                budgetId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { budgetId -> navController.navigate(Screen.EditBudget.navRoute(budgetId)) },
                onNavigateToTransactionDetail = { id -> navController.navigate(Screen.TransactionDetail.navRoute(id)) }
            )
        }

        // ── Goal sub-screens ─────────────────────────────────────────────────
        composable(Screen.CreateGoal.route) {
            CreateGoalScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(
            route     = Screen.EditGoal.routeWithArg,
            arguments = listOf(
                navArgument(Screen.EditGoal.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            CreateGoalScreen(
                onNavigateBack = { navController.popBackStack() },
                goalId = backStackEntry.arguments?.getLong(Screen.EditGoal.ARG_ID)
            )
        }

        composable(
            route     = Screen.GoalDetail.routeWithArg,
            arguments = listOf(
                navArgument(Screen.GoalDetail.ARG_ID) { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(Screen.GoalDetail.ARG_ID) ?: 0L
            GoalDetailScreen(
                goalId = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { goalId -> navController.navigate(Screen.EditGoal.navRoute(goalId)) }
            )
        }
    }
}

// ─── Shared placeholder ───────────────────────────────────────────────────────

/**
 * Temporary full-screen composable used until real screens are implemented.
 * Replace with the actual screen composable in the relevant route lambda.
 */
@Composable
internal fun PlaceholderScreen(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier          = modifier.fillMaxSize(),
        contentAlignment  = Alignment.Center
    ) {
        Text(
            text  = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
