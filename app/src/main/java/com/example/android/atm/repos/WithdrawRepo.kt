package com.example.android.atm.repos

import com.example.android.atm.db.Customer
import com.example.android.atm.db.CustomerDao
import com.example.android.atm.db.Transaction
import com.example.android.atm.db.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class WithdrawRepo(private val customerDao: CustomerDao, private val transactionDao: TransactionDao) {

    suspend fun withdraw(customerId: Long, amount: Double): Customer? {
        return withContext(Dispatchers.IO) {
            customerDao.canWithdraw(customerId, amount)
        }
    }

    suspend fun debitCustomer(customer: Customer, amount: Double) {
        withContext(Dispatchers.IO) {
            customer.balance = customer.balance - amount
            customerDao.updateCustomer(customer)
        }
    }

    suspend fun saveDebit(customer: Customer, amount: Double): Long {
        return withContext(Dispatchers.IO) {
            val date = Calendar.getInstance().time
            val balance = customer.balance
            val transaction = Transaction(0, customer.id, date, amount, balance, "Lagos")
            transactionDao.saveTransaction(transaction)
        }
    }

}