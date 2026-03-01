package com.usj.fintrack.di

import com.usj.fintrack.data.repository.AccountRepositoryImpl
import com.usj.fintrack.data.repository.BudgetRepositoryImpl
import com.usj.fintrack.data.repository.CategoryRepositoryImpl
import com.usj.fintrack.data.repository.GoalRepositoryImpl
import com.usj.fintrack.data.repository.TransactionRepositoryImpl
import com.usj.fintrack.data.repository.UserRepositoryImpl
import com.usj.fintrack.domain.repository.AccountRepository
import com.usj.fintrack.domain.repository.BudgetRepository
import com.usj.fintrack.domain.repository.CategoryRepository
import com.usj.fintrack.domain.repository.GoalRepository
import com.usj.fintrack.domain.repository.TransactionRepository
import com.usj.fintrack.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(
        impl: GoalRepositoryImpl
    ): GoalRepository
}
