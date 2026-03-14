package com.usj.fintrack.domain.usecase.seed

import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.model.Budget
import com.usj.fintrack.domain.model.Category
import com.usj.fintrack.domain.model.Goal
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.User
import com.usj.fintrack.domain.model.enum.AccountType
import com.usj.fintrack.domain.model.enum.BudgetStatusType
import com.usj.fintrack.domain.model.enum.Currency
import com.usj.fintrack.domain.model.enum.TransactionType
import com.usj.fintrack.domain.repository.AccountRepository
import com.usj.fintrack.domain.repository.BudgetRepository
import com.usj.fintrack.domain.repository.CategoryRepository
import com.usj.fintrack.domain.repository.GoalRepository
import com.usj.fintrack.domain.repository.TransactionRepository
import com.usj.fintrack.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class SeedDatabaseUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke() {
        val userId = seedDefaultUser()
        val categories = seedDefaultCategories()
        val accounts = seedDemoAccounts(userId)

        seedDemoTransactions(accounts, categories)
        seedDemoBudgets(userId, categories)
        seedDemoGoals(userId)
    }

    // ─── Core seed (User + Categories) ───────────────────────────────────────

    private suspend fun seedDefaultUser(): Long {
        val existingById = userRepository.getUserById(DEMO_USER_ID)
        if (existingById != null) return existingById.id

        val existingByEmail = userRepository.getUserByEmail(DEMO_USER_EMAIL)
        if (existingByEmail != null) return existingByEmail.id

        return userRepository.insertUser(
            User(
                id = DEMO_USER_ID,
                email = DEMO_USER_EMAIL,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    private suspend fun seedDefaultCategories(): List<Category> {
        val categories = categoryRepository.getAllCategories().first()
        val hasDefaults = categories.any { it.isDefault }

        if (!hasDefaults) {
            categoryRepository.insertDefaultCategories(defaultCategories())
        }

        return categoryRepository.getAllCategories().first()
    }

    // ─── Demo seed (Accounts + Transactions + Budgets + Goals) ──────────────

    private suspend fun seedDemoAccounts(userId: Long): List<Account> {
        val existingAccounts = accountRepository.getAllAccounts().first()
        if (existingAccounts.isNotEmpty()) return existingAccounts

        val now = System.currentTimeMillis()
        val demoAccounts = listOf(
            Account(
                userId = userId,
                name = "Main Checking",
                type = AccountType.CHECKING,
                balance = 2140.0,
                currency = Currency.EUR,
                createdAt = now - (90 * DAY_MS)
            ),
            Account(
                userId = userId,
                name = "Emergency Savings",
                type = AccountType.SAVINGS,
                balance = 6200.0,
                currency = Currency.EUR,
                createdAt = now - (120 * DAY_MS)
            ),
            Account(
                userId = userId,
                name = "Credit Card",
                type = AccountType.CREDIT_CARD,
                balance = -320.0,
                currency = Currency.EUR,
                createdAt = now - (180 * DAY_MS)
            )
        )

        demoAccounts.forEach { accountRepository.createAccount(it) }
        return accountRepository.getAllAccounts().first()
    }

    private suspend fun seedDemoTransactions(
        accounts: List<Account>,
        categories: List<Category>
    ) {
        val existingTransactions = transactionRepository.getAllTransactions().first()
        if (existingTransactions.isNotEmpty()) return

        val accountByName = accounts.associateBy { it.name }
        val categoryByName = categories.associateBy { it.name }

        val checking = accountByName["Main Checking"] ?: return
        val creditCard = accountByName["Credit Card"] ?: return
        val cashLikeAccount = checking

        val food = categoryByName["Food"] ?: return
        val transport = categoryByName["Transport"] ?: return
        val entertainment = categoryByName["Entertainment"] ?: return
        val health = categoryByName["Health"] ?: return
        val housing = categoryByName["Housing"] ?: return
        val salary = categoryByName["Salary"] ?: return
        val other = categoryByName["Other"] ?: return

        val now = System.currentTimeMillis()
        val demoTransactions = listOf(
            Transaction(
                accountId = checking.id,
                amount = 2800.0,
                type = TransactionType.INCOME,
                date = now - (12 * DAY_MS),
                description = "Salary - March",
                categories = listOf(salary),
                location = "USJ"
            ),
            Transaction(
                accountId = checking.id,
                amount = 520.0,
                type = TransactionType.INCOME,
                date = now - (8 * DAY_MS),
                description = "Freelance project",
                categories = listOf(other),
                location = "Remote"
            ),
            Transaction(
                accountId = checking.id,
                amount = 950.0,
                type = TransactionType.EXPENSE,
                date = now - (10 * DAY_MS),
                description = "Rent",
                categories = listOf(housing),
                location = "Zaragoza"
            ),
            Transaction(
                accountId = checking.id,
                amount = 210.0,
                type = TransactionType.EXPENSE,
                date = now - (6 * DAY_MS),
                description = "Supermarket",
                categories = listOf(food),
                location = "Mercadona"
            ),
            Transaction(
                accountId = checking.id,
                amount = 60.0,
                type = TransactionType.EXPENSE,
                date = now - (5 * DAY_MS),
                description = "Metro card",
                categories = listOf(transport),
                location = "Zaragoza"
            ),
            Transaction(
                accountId = creditCard.id,
                amount = 38.0,
                type = TransactionType.EXPENSE,
                date = now - (4 * DAY_MS),
                description = "Cinema night",
                categories = listOf(entertainment),
                location = "Cinesa"
            ),
            Transaction(
                accountId = creditCard.id,
                amount = 12.0,
                type = TransactionType.EXPENSE,
                date = now - (3 * DAY_MS),
                description = "Streaming subscription",
                categories = listOf(entertainment),
                location = "Online"
            ),
            Transaction(
                accountId = cashLikeAccount.id,
                amount = 16.0,
                type = TransactionType.EXPENSE,
                date = now - (2 * DAY_MS),
                description = "Coffee with friends",
                categories = listOf(food),
                location = "Cafe Central"
            ),
            Transaction(
                accountId = checking.id,
                amount = 24.0,
                type = TransactionType.EXPENSE,
                date = now - (1 * DAY_MS),
                description = "Pharmacy",
                categories = listOf(health),
                location = "Farmacia"
            )
        )

        demoTransactions.forEach { transactionRepository.createTransaction(it) }
    }

    private suspend fun seedDemoBudgets(userId: Long, categories: List<Category>) {
        val existingBudgets = budgetRepository.getAllBudgets().first()
        if (existingBudgets.isNotEmpty()) return

        val categoryByName = categories.associateBy { it.name }
        val food = categoryByName["Food"] ?: return
        val transport = categoryByName["Transport"] ?: return
        val entertainment = categoryByName["Entertainment"] ?: return
        val health = categoryByName["Health"] ?: return
        val housing = categoryByName["Housing"] ?: return

        val periodStart = startOfCurrentMonthMillis()
        val periodEnd = endOfCurrentMonthMillis()

        val demoBudgets = listOf(
            Budget(
                userId = userId,
                categoryId = food.id,
                limitAmount = 250.0,
                spentAmount = 226.0,
                periodStartDate = periodStart,
                periodEndDate = periodEnd,
                status = BudgetStatusType.WARNING
            ),
            Budget(
                userId = userId,
                categoryId = transport.id,
                limitAmount = 120.0,
                spentAmount = 60.0,
                periodStartDate = periodStart,
                periodEndDate = periodEnd,
                status = BudgetStatusType.ON_TRACK
            ),
            Budget(
                userId = userId,
                categoryId = entertainment.id,
                limitAmount = 40.0,
                spentAmount = 50.0,
                periodStartDate = periodStart,
                periodEndDate = periodEnd,
                status = BudgetStatusType.EXCEEDED
            ),
            Budget(
                userId = userId,
                categoryId = health.id,
                limitAmount = 80.0,
                spentAmount = 24.0,
                periodStartDate = periodStart,
                periodEndDate = periodEnd,
                status = BudgetStatusType.ON_TRACK
            ),
            Budget(
                userId = userId,
                categoryId = housing.id,
                limitAmount = 1000.0,
                spentAmount = 950.0,
                periodStartDate = periodStart,
                periodEndDate = periodEnd,
                status = BudgetStatusType.WARNING
            )
        )

        demoBudgets.forEach { budgetRepository.createBudget(it) }
    }

    private suspend fun seedDemoGoals(userId: Long) {
        val existingGoals = goalRepository.getAllGoals().first()
        if (existingGoals.isNotEmpty()) return

        val now = System.currentTimeMillis()
        val demoGoals = listOf(
            Goal(
                userId = userId,
                name = "Emergency Fund",
                targetAmount = 10000.0,
                currentAmount = 6200.0,
                deadline = now + (240 * DAY_MS),
                isCompleted = false
            ),
            Goal(
                userId = userId,
                name = "Summer Trip",
                targetAmount = 1800.0,
                currentAmount = 980.0,
                deadline = now + (120 * DAY_MS),
                isCompleted = false
            ),
            Goal(
                userId = userId,
                name = "Laptop Upgrade",
                targetAmount = 1400.0,
                currentAmount = 1400.0,
                deadline = now + (60 * DAY_MS),
                isCompleted = true
            )
        )

        demoGoals.forEach { goalRepository.createGoal(it) }
    }

    private fun defaultCategories() = listOf(
        Category(name = "Food",          iconName = "restaurant",     colorHex = "#FF9800", isDefault = true), // Orange
        Category(name = "Transport",     iconName = "directions_car", colorHex = "#2196F3", isDefault = true), // Blue
        Category(name = "Entertainment", iconName = "movie",          colorHex = "#9C27B0", isDefault = true), // Purple
        Category(name = "Health",        iconName = "favorite",       colorHex = "#E91E63", isDefault = true), // Pink
        Category(name = "Housing",       iconName = "home",           colorHex = "#4CAF50", isDefault = true), // Green
        Category(name = "Salary",        iconName = "attach_money",   colorHex = "#009688", isDefault = true), // Teal
        Category(name = "Other",         iconName = "category",       colorHex = "#607D8B", isDefault = true), // Blue-grey
    )

    private fun startOfCurrentMonthMillis(): Long = LocalDate.now()
        .withDayOfMonth(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    private fun endOfCurrentMonthMillis(): Long = LocalDate.now()
        .withDayOfMonth(LocalDate.now().lengthOfMonth())
        .atStartOfDay(ZoneId.systemDefault())
        .plusDays(1)
        .minusNanos(1)
        .toInstant()
        .toEpochMilli()

    companion object {
        private const val DAY_MS = 24L * 60L * 60L * 1000L
        private const val DEMO_USER_ID = 1L
        private const val DEMO_USER_EMAIL = "profesor@usj.com"
    }
}
