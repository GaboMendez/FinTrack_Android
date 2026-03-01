package com.usj.fintrack.data.repository

import com.usj.fintrack.data.dao.AccountDao
import com.usj.fintrack.data.mapper.toDomain
import com.usj.fintrack.data.mapper.toEntity
import com.usj.fintrack.domain.model.Account
import com.usj.fintrack.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAllAccounts(): Flow<List<Account>> =
        accountDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getAccountById(id: Long): Account? =
        accountDao.getById(id)?.toDomain()

    override suspend fun createAccount(account: Account): Long =
        accountDao.insert(account.toEntity())

    override suspend fun updateAccount(account: Account) =
        accountDao.update(account.toEntity())

    override suspend fun deleteAccount(id: Long) {
        val entity = accountDao.getById(id) ?: return
        accountDao.delete(entity)
    }

    override suspend fun updateAccountBalance(id: Long, newBalance: Double) =
        accountDao.updateBalance(id, newBalance)
}
