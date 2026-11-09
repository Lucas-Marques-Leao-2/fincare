package com.example.fincare.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
class Account(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String,
    var bankName: String,
    var kind: String,
    var balance: Double,
    var main: Boolean = false,
)
