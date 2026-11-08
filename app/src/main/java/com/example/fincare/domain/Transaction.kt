package com.example.fincare.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
class Transaction(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var description: String,
    var category: String,
    var amount: Double,
    var type: String,
    var date: String,
    var bankCode: String,
    var bankName: String,
)
