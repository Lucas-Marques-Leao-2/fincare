package com.example.fincare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fincare.domain.Contact

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<Contact>)

    @Insert
    suspend fun insert(contact: Contact)

    @Query("SELECT * FROM Contact")
    suspend fun findAll(): List<Contact>
}