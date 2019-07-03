package com.example.android.atm.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(customer: List<Customer>)

    @Query("SELECT * FROM customer where id =:customerId")
    fun getCustomer(customerId: Long): LiveData<Customer>

    @Query("SELECT * FROM customer where accountNo=:accountNo and pin =:pin")
    fun verifyPin(accountNo: String, pin: Int): Customer?

    @Query("SELECT * FROM customer where accountNo=:accountNo and blocked =0")
    fun accountEnabled(accountNo: String): Boolean

    @Query("SELECT * FROM customer where accountNo=:accountNo")
    fun accountExist(accountNo: String): Boolean

    @Query("SELECT * FROM customer where accountNo =:accountNo")
    fun getCustomerByAccount(accountNo: String): LiveData<Customer>

    @Query("UPDATE customer SET blocked = 1 where accountNo=:accountNo")
    fun blockAccount(accountNo: String)

    @Query("SELECT * FROM customer where id=:customerId and balance >= :amountToWithdraw")
    fun canWithdraw(customerId: Long, amountToWithdraw: Double): Customer?

    @Query("SELECT balance FROM customer where id=:customerId")
    fun getBalance(customerId: Long): LiveData<Double>

    @Update
    fun updateCustomer(customer: Customer)

}