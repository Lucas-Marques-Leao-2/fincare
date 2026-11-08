package com.example.fincare.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investments")
class Investment(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String,
    var type: String,
    var quantity: Double,
    var averagePrice: Double,
    var currentPrice: Double,
    var month: Int,
    var year: Int,
) {
    val amountInvested: Double
        get() = quantity * averagePrice

    val currentValue: Double
        get() = quantity * currentPrice

    val gainLoss: Double
        get() = currentValue - amountInvested
}
