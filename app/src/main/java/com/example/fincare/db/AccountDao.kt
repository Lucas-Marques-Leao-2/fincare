package com.example.fincare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fincare.domain.Account
import com.example.fincare.sample.sampleDataAccounts

@Dao
interface AccountDao {
    @Insert
    suspend fun insertAll(accounts: List<Account>)

    @Insert
    suspend fun insert(account: Account)

    @Query("SELECT * FROM accounts ORDER BY main DESC, balance DESC")
    suspend fun findAll(): List<Account>

    @Query("UPDATE accounts SET main = 0")
    suspend fun clearMain()

    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun count(): Int
}

suspend fun AccountDao.findAllSeedingSampleIfEmpty(): List<Account> {
    if (count() == 0) {
        insertAll(sampleDataAccounts)
    }
    return findAll()
}
