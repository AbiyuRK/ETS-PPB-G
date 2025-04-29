package com.example.rupiahtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val category: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis()
)
