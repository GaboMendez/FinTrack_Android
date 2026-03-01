package com.usj.fintrack.data.repository

import com.usj.fintrack.data.dao.TransactionDao
import com.usj.fintrack.data.entity.TransactionCategoryCrossRef
import com.usj.fintrack.data.mapper.toDomain
import com.usj.fintrack.data.mapper.toEntity
import com.usj.fintrack.domain.model.Transaction
import com.usj.fintrack.domain.model.TransactionStats
import com.usj.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllWithCategories().map { list ->
            list.map { withCats ->
                withCats.transaction.toDomain(withCats.categories.map { it.toDomain() })
            }
        }

    override fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>> =
        transactionDao.getByAccountWithCategories(accountId).map { list ->
            list.map { withCats ->
                withCats.transaction.toDomain(withCats.categories.map { it.toDomain() })
            }
        }

    override suspend fun getTransactionById(id: Long): Transaction? {
        val entity = transactionDao.getById(id) ?: return null
        val withCategories = transactionDao.getTransactionWithCategories(id)
        return entity.toDomain(withCategories?.categories?.map { it.toDomain() } ?: emptyList())
    }

    override suspend fun createTransaction(transaction: Transaction): Result<Long> {
        return try {
            val id = transactionDao.insert(transaction.toEntity())
            transaction.categories.forEach { category ->
                transactionDao.insertTransactionCategory(
                    TransactionCategoryCrossRef(transactionId = id, categoryId = category.id)
                )
            }
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        return try {
            transactionDao.update(transaction.toEntity())
            // Replace all category links for this transaction
            transactionDao.deleteTransactionCategories(transaction.id)
            transaction.categories.forEach { category ->
                transactionDao.insertTransactionCategory(
                    TransactionCategoryCrossRef(transactionId = transaction.id, categoryId = category.id)
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: Long): Result<Unit> {
        return try {
            val entity = transactionDao.getById(transactionId)
                ?: return Result.failure(NoSuchElementException("Transaction $transactionId not found"))
            // Cross-ref rows are removed by ON DELETE CASCADE on the FK
            transactionDao.delete(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTransactionStats(startDate: Long, endDate: Long): TransactionStats {
        val income = transactionDao.getSumByType("INCOME", startDate, endDate) ?: 0.0
        val expense = transactionDao.getSumByType("EXPENSE", startDate, endDate) ?: 0.0
        return TransactionStats(
            totalIncome = income,
            totalExpense = expense,
            balance = income - expense
        )
    }
}
