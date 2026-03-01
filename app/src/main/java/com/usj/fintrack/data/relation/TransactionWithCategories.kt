package com.usj.fintrack.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.usj.fintrack.data.entity.CategoryEntity
import com.usj.fintrack.data.entity.TransactionCategoryCrossRef
import com.usj.fintrack.data.entity.TransactionEntity

/**
 * Room relation: many-to-many between [TransactionEntity] and [CategoryEntity]
 * resolved via [TransactionCategoryCrossRef] junction table.
 */
data class TransactionWithCategories(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "transaction_id",
        entityColumn = "category_id",
        associateBy = Junction(
            value = TransactionCategoryCrossRef::class,
            parentColumn = "transaction_id",
            entityColumn = "category_id"
        )
    )
    val categories: List<CategoryEntity>
)
