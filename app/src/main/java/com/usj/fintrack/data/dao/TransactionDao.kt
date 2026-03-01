package com.usj.fintrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.usj.fintrack.data.entity.TransactionCategoryCrossRef
import com.usj.fintrack.data.entity.TransactionEntity
import com.usj.fintrack.data.relation.TransactionWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransactionCategory(crossRef: TransactionCategoryCrossRef)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transaction_id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE account_id = :accountId ORDER BY date DESC")
    fun getByAccount(accountId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    suspend fun getByDateRange(startDate: Long, endDate: Long): List<TransactionEntity>

    @Transaction
    @Query("SELECT * FROM transactions WHERE transaction_id = :id")
    suspend fun getTransactionWithCategories(id: Long): TransactionWithCategories?

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllWithCategories(): Flow<List<TransactionWithCategories>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE account_id = :accountId ORDER BY date DESC")
    fun getByAccountWithCategories(accountId: Long): Flow<List<TransactionWithCategories>>

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE type = :type AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getSumByType(type: String, startDate: Long, endDate: Long): Double?

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transaction_category WHERE transaction_id = :transactionId")
    suspend fun deleteTransactionCategories(transactionId: Long)
}
