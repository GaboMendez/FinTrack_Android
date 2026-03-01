package com.usj.fintrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val categoryId: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "icon_name")
    val iconName: String,

    @ColumnInfo(name = "color_hex")
    val colorHex: String,

    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false
)
