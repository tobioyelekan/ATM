package com.example.android.atm.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTransaction(transaction: Transaction): Long

    @Query("SELECT * FROM transaction_table where customerId=:customerId")
    fun getAllTransactions(customerId: Long): LiveData<List<Transaction>?>

    @Query("SELECT * FROM transaction_table where id =:id")
    fun getTransaction(id: Long): LiveData<Transaction>

}