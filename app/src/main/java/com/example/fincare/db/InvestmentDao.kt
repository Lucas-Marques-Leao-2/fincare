package com.example.fincare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fincare.domain.Investment
import com.example.fincare.sample.sampleDataInvestments

@Dao
interface InvestmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(investments: List<Investment>)

    @Insert
    suspend fun insert(investment: Investment)

    @Query("SELECT * FROM investments")
    suspend fun findAll(): List<Investment>

    @Query("SELECT COUNT(*) FROM investments")
    suspend fun count(): Int
}

suspend fun InvestmentDao.findAllSeedingSampleIfEmpty(): List<Investment> {
    if (count() == 0) {
        insertAll(sampleDataInvestments)
    }
    return findAll()
}
