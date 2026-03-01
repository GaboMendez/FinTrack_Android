package com.usj.fintrack.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.usj.fintrack.data.database.Converters
import com.usj.fintrack.data.dao.AccountDao
import com.usj.fintrack.data.dao.BudgetDao
import com.usj.fintrack.data.dao.CategoryDao
import com.usj.fintrack.data.dao.GoalDao
import com.usj.fintrack.data.dao.TransactionDao
import com.usj.fintrack.data.dao.UserDao
import com.usj.fintrack.data.entity.AccountEntity
import com.usj.fintrack.data.entity.BudgetEntity
import com.usj.fintrack.data.entity.CategoryEntity
import com.usj.fintrack.data.entity.GoalEntity
import com.usj.fintrack.data.entity.TransactionCategoryCrossRef
import com.usj.fintrack.data.entity.TransactionEntity
import com.usj.fintrack.data.entity.UserEntity
import com.usj.fintrack.data.entity.UserProfileEntity

@Database(
    entities = [
        UserEntity::class,
        UserProfileEntity::class,
        AccountEntity::class,
        TransactionEntity::class,
        CategoryEntity::class,
        TransactionCategoryCrossRef::class,
        BudgetEntity::class,
        GoalEntity::class
    ],
    version = 1,
    exportSchema = true
)
//@TypeConverters(Converters::class)
abstract class FinTrackDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao

    companion object {
        const val DATABASE_NAME = "fintrack_db"
        // Builder note: call .fallbackToDestructiveMigration() on Room.databaseBuilder()
        // in DatabaseModule during development. Remove before production release.
    }
}
