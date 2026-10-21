package com.example.fincare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fincare.domain.Accommodation

@Dao
interface AccommodationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accommodations: List<Accommodation>)

    @Insert
    suspend fun insert(accommodation: Accommodation)

    @Query("SELECT * FROM Accommodation")
    suspend fun findAll(): List<Accommodation>

}