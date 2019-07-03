package com.example.android.atm.ui.viewmodels

import android.app.Application
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.example.android.atm.db.BankDatabase
import com.example.android.atm.repos.TransactionSuccessRepo
import java.text.SimpleDateFormat
import java.util.*

class TransactionSuccessfulViewModel(
    private val transId: Long,
    application: Application
) : ViewModel() {

    private val transDao = BankDatabase.getInstance(application).transactionDaoo
    private val repo = TransactionSuccessRepo(transDao)

    val transactionDetails = repo.transactionDetails

    val date = Transformations.map(transactionDetails) {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault())
        dateFormat.format(it.date)
    }

    val amount = Transformations.map(transactionDetails) {
        "₦" + it.amount.toString()
    }

    val balance = Transformations.map(transactionDetails) {
        "₦" + it.balance.toString()
    }

    val location = Transformations.map(transactionDetails) {
        it.location
    }

    init {
        getTransactionDetails()
    }

    fun getTransactionDetails() {
        repo.getTransactionDetails(transId)
    }

}
