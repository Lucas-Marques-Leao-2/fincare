package com.example.fincare.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
class Contact (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String,
    var number: String,
)