package com.usj.fintrack.data.repository

import com.usj.fintrack.data.dao.UserDao
import com.usj.fintrack.data.mapper.toDomain
import com.usj.fintrack.data.mapper.toEntity
import com.usj.fintrack.domain.model.User
import com.usj.fintrack.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUserById(id: Long): User? =
        userDao.getUserById(id)?.toDomain()

    override suspend fun getUserByEmail(email: String): User? =
        userDao.getUserByEmail(email)?.toDomain()

    override suspend fun insertUser(user: User): Long =
        userDao.insert(user.toEntity())

    override suspend fun updateUser(user: User) =
        userDao.update(user.toEntity())

    override suspend fun deleteUser(userId: Long): Result<Unit> {
        return try {
            val entity = userDao.getUserById(userId)
                ?: return Result.failure(NoSuchElementException("User $userId not found"))
            userDao.delete(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
