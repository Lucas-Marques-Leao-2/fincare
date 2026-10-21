package com.example.fincare.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
class Accommodation (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var total: Double,
    var rating: Double,
    var imageUrl: String,
    var location: String,
    var host: String,
    var dates: String

)