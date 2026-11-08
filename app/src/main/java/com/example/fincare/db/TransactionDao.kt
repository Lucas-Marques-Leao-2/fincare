package com.example.fincare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fincare.domain.Transaction

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions")
    suspend fun findAll(): List<Transaction>
}
