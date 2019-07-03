package com.example.android.atm.repos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.atm.db.TransactionDao

class TransactionSuccessRepo(private val transactionDao: TransactionDao) {

    private val transaction_id = MutableLiveData<Long>()

    val transactionDetails = Transformations.switchMap(transaction_id) {
        transactionDao.getTransaction(it)
    }

    fun getTransactionDetails(transId: Long) {
        transaction_id.value = transId
    }

}