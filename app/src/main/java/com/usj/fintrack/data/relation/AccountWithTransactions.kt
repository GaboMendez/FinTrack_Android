package com.usj.fintrack.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.usj.fintrack.data.entity.AccountEntity
import com.usj.fintrack.data.entity.TransactionEntity

/**
 * Room relation: one-to-many between [AccountEntity] and [TransactionEntity].
 */
data class AccountWithTransactions(
    @Embedded
    val account: AccountEntity,

    @Relation(
        parentColumn = "account_id",
        entityColumn = "account_id"
    )
    val transactions: List<TransactionEntity>
)
