package com.usj.fintrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "transaction_category",
    primaryKeys = ["transaction_id", "category_id"],
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["transaction_id"],
            childColumns = ["transaction_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("category_id")]
)
data class TransactionCategoryCrossRef(
    @ColumnInfo(name = "transaction_id")
    val transactionId: Long,

    @ColumnInfo(name = "category_id")
    val categoryId: Long
)
