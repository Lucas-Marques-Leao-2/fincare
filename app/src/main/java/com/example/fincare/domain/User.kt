package com.example.fincare.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String,
    var email: String,
    var phone: String,
    var password: String,
    var state: String,
)
