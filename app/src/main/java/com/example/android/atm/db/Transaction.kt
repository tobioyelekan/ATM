package com.example.android.atm.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val customerId: Long,
    val date: Date?,
    val amount: Double,
    val balance: Double,
    val location: String
)