     package com.usj.fintrack.di

import android.content.Context
import androidx.room.Room
import com.usj.fintrack.data.dao.AccountDao
import com.usj.fintrack.data.dao.BudgetDao
import com.usj.fintrack.data.dao.CategoryDao
import com.usj.fintrack.data.dao.GoalDao
import com.usj.fintrack.data.dao.TransactionDao
import com.usj.fintrack.data.dao.UserDao
import com.usj.fintrack.data.database.FinTrackDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFinTrackDatabase(
        @ApplicationContext context: Context
    ): FinTrackDatabase = Room.databaseBuilder(
        context,
        FinTrackDatabase::class.java,
        FinTrackDatabase.DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideUserDao(database: FinTrackDatabase): UserDao =
        database.userDao()

    @Provides
    fun provideAccountDao(database: FinTrackDatabase): AccountDao =
        database.accountDao()

    @Provides
    fun provideTransactionDao(database: FinTrackDatabase): TransactionDao =
        database.transactionDao()

    @Provides
    fun provideCategoryDao(database: FinTrackDatabase): CategoryDao =
        database.categoryDao()

    @Provides
    fun provideBudgetDao(database: FinTrackDatabase): BudgetDao =
        database.budgetDao()

    @Provides
    fun provideGoalDao(database: FinTrackDatabase): GoalDao =
        database.goalDao()
}
