package com.usj.fintrack.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.usj.fintrack.data.entity.UserEntity
import com.usj.fintrack.data.entity.UserProfileEntity

/**
 * Room relation: one-to-one between [UserEntity] and [UserProfileEntity].
 * [profile] is nullable because a user may exist before their profile is created.
 */
data class UserWithProfile(
    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_owner_id"
    )
    val profile: UserProfileEntity?
)
