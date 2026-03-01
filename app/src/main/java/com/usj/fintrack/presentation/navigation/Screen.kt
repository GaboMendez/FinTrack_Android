package com.usj.fintrack.presentation.navigation

sealed class Screen(val route: String) {
    // ── Auth flow ────────────────────────────────────────────────────────────
    object Splash       : Screen("splash")
    object Onboarding   : Screen("onboarding")
    object ProfileSetup : Screen("profile_setup")

    // ── Main bottom-nav destinations ─────────────────────────────────────────
    object Dashboard    : Screen("dashboard")
    object Transactions : Screen("transactions")
    object Accounts     : Screen("accounts")
    object Budgets      : Screen("budgets")
    object Goals        : Screen("goals")

    // ── Transaction sub-screens ───────────────────────────────────────────────
    object CreateTransaction : Screen("create_transaction")

    /** Deep-link: `edit_transaction/{transactionId}` */
    object EditTransaction : Screen("edit_transaction") {
        const val ARG_ID = "transactionId"
        val routeWithArg get() = "$route/{$ARG_ID}"
        fun navRoute(id: Long) = "$route/$id"
    }

    /** Deep-link: `transaction_detail/{transactionId}` */
    object TransactionDetail : Screen("transaction_detail") {
        const val ARG_ID = "transactionId"
        /** Full composable route pattern with mandatory arg. */
        val routeWithArg get() = "$route/{$ARG_ID}"
        /** Navigation target for a specific transaction. */
        fun navRoute(id: Long) = "$route/$id"
    }

    object TicketCapture : Screen("ticket_capture")

    /** Deep-link: `transaction_review?imageUri={imageUri}` */
    object TransactionReview : Screen("transaction_review") {
        const val ARG_IMAGE_URI = "imageUri"
        /** Full composable route pattern with optional arg. */
        val routeWithArg get() = "$route?$ARG_IMAGE_URI={$ARG_IMAGE_URI}"
        /** Navigation target with an optional image URI. */
        fun navRoute(imageUri: String? = null) =
            if (imageUri != null) "$route?$ARG_IMAGE_URI=$imageUri" else route
    }

    // ── Account sub-screens ───────────────────────────────────────────────────
    object CreateAccount : Screen("create_account")

    /** Deep-link: `edit_account/{accountId}` */
    object EditAccount : Screen("edit_account") {
        const val ARG_ID = "accountId"
        val routeWithArg get() = "$route/{$ARG_ID}"
        fun navRoute(id: Long) = "$route/$id"
    }

    /** Deep-link: `account_detail/{accountId}` */
    object AccountDetail : Screen("account_detail") {
        const val ARG_ID = "accountId"
        val routeWithArg get() = "$route/{$ARG_ID}"
        fun navRoute(id: Long) = "$route/$id"
    }

    // ── Budget sub-screens ────────────────────────────────────────────────────
    object CreateBudget : Screen("create_budget")

    /** Deep-link: `budget_detail/{budgetId}` */
    object BudgetDetail : Screen("budget_detail") {
        const val ARG_ID = "budgetId"
        val routeWithArg get() = "$route/{$ARG_ID}"
        fun navRoute(id: Long) = "$route/$id"
    }

    // ── Goal sub-screens ──────────────────────────────────────────────────────
    object CreateGoal : Screen("create_goal")

    /** Deep-link: `goal_detail/{goalId}` */
    object GoalDetail : Screen("goal_detail") {
        const val ARG_ID = "goalId"
        val routeWithArg get() = "$route/{$ARG_ID}"
        fun navRoute(id: Long) = "$route/$id"
    }
}

/** Routes for which the bottom navigation bar should be visible. */
val mainScreenRoutes = setOf(
    Screen.Dashboard.route,
    Screen.Transactions.route,
    Screen.Accounts.route,
    Screen.Budgets.route,
    Screen.Goals.route
)
