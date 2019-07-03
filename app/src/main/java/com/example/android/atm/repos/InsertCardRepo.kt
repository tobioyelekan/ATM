package com.example.android.atm.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.atm.db.Customer
import com.example.android.atm.db.CustomerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertCardRepo(private val customerDao: CustomerDao) {

    suspend fun canProceed(accountNo: String): AccountStatus {
        return withContext(Dispatchers.IO) {
            val exist = customerDao.accountExist(accountNo)
            if (exist) {
                val enabled = customerDao.accountEnabled(accountNo)
                if (enabled) {
                    AccountStatus.PROCEED
                } else {
                    AccountStatus.BLOCKED
                }
            } else {
                AccountStatus.NOTEXIST
            }
        }
    }

    enum class AccountStatus { PROCEED, BLOCKED, NOTEXIST }
}