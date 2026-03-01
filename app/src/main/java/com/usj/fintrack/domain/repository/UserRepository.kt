package com.usj.fintrack.domain.repository

import com.usj.fintrack.domain.model.User

interface UserRepository {
    suspend fun getUserById(id: Long): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: Long): Result<Unit>
}
