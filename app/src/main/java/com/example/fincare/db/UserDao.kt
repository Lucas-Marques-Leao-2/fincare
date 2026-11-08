package com.example.fincare.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fincare.domain.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email COLLATE NOCASE LIMIT 1")
    suspend fun findByEmail(email: String): User?
}
