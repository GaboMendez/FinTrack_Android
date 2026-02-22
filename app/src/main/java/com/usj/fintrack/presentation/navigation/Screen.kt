package com.usj.fintrack.presentation.navigation

sealed class Screen(val route: String) {
    // Auth flow
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object ProfileSetup : Screen("profile_setup")

    // Main bottom-nav destinations
    object Dashboard : Screen("dashboard")
    object Transactions : Screen("transactions")
    object Accounts : Screen("accounts")
    object Budgets : Screen("budgets")
    object Goals : Screen("goals")

    // Transaction sub-screens
    object CreateTransaction : Screen("create_transaction")
    object TransactionDetail : Screen("transaction_detail")
    object TicketCapture : Screen("ticket_capture")
    object TransactionReview : Screen("transaction_review")

    // Account sub-screens
    object CreateAccount : Screen("create_account")
    object AccountDetail : Screen("account_detail")

    // Budget sub-screens
    object CreateBudget : Screen("create_budget")
    object BudgetDetail : Screen("budget_detail")

    // Goal sub-screens
    object CreateGoal : Screen("create_goal")
    object GoalDetail : Screen("goal_detail")
}

/** Routes for which the bottom navigation bar should be visible. */
val mainScreenRoutes = setOf(
    Screen.Dashboard.route,
    Screen.Transactions.route,
    Screen.Accounts.route,
    Screen.Budgets.route,
    Screen.Goals.route
)
