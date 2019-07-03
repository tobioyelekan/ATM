package com.example.android.atm.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val accountNo: String,
    val name: String,
    var balance: Double,
    val pin: Int,
    var blocked: Boolean
) {
    companion object {
        fun populateData() = listOf(
            Customer(0, "1234567890", "Tobi", 10000.0, 1234, false),
            Customer(0, "1234567891", "Tosin", 20000.0, 4321, false)
        )
    }
}