package com.usj.fintrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_profiles",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_owner_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_owner_id")]
)
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profile_id")
    val profileId: Long = 0,

    @ColumnInfo(name = "user_owner_id")
    val userOwnerId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,

    @ColumnInfo(name = "currency")
    val currency: String = "EUR",

    @ColumnInfo(name = "preferred_language")
    val preferredLanguage: String = "es"
)
