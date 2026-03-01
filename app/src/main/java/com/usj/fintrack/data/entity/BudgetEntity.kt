package com.usj.fintrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id"), Index("category_id")]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    val budgetId: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "category_id")
    val categoryId: Long,

    @ColumnInfo(name = "limit_amount")
    val limitAmount: Double,

    @ColumnInfo(name = "spent_amount")
    val spentAmount: Double = 0.0,

    @ColumnInfo(name = "period_start_date")
    val periodStartDate: Long,

    @ColumnInfo(name = "period_end_date")
    val periodEndDate: Long,

    @ColumnInfo(name = "status")
    val status: String = "ON_TRACK"
)
