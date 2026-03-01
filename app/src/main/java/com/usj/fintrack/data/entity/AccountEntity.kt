package com.usj.fintrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id")]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    val accountId: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "balance")
    val balance: Double = 0.0,

    @ColumnInfo(name = "currency")
    val currency: String = "EUR",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
