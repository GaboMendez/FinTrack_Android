package com.usj.fintrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("account_id"),
        Index("date"),
        Index("type")
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionId: Long = 0,

    @ColumnInfo(name = "account_id")
    val accountId: Long,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "image_uri")
    val imageUri: String? = null,

    @ColumnInfo(name = "location")
    val location: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
