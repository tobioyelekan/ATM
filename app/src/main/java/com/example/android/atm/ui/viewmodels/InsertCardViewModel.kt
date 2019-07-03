package com.example.android.atm.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.atm.db.BankDatabase
import com.example.android.atm.repos.InsertCardRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.android.atm.repos.InsertCardRepo.AccountStatus

class InsertCardViewModel(application: Application) : AndroidViewModel(application) {

    private val job = Job()
    private val viewModelScope = CoroutineScope(job + Dispatchers.Main)

    private val dao = BankDatabase.getInstance(application).customerDao
    private val repo = InsertCardRepo(dao)

    val accountNumber = MutableLiveData<String>()

    private val _validate = MutableLiveData<String?>()
    val validate: LiveData<String?>
        get() = _validate

    private val _navigateToEnterPin = MutableLiveData<String>()
    val navigateToEnterPin: LiveData<String>
        get() = _navigateToEnterPin

    init {
        accountNumber.value = ""
    }

    fun onCardInserted() {
        when {
            accountNumber.value!!.isEmpty() -> _validate.value = "cannot be empty"
            accountNumber.value!!.length < 10 -> _validate.value = "invalid account number"
            else -> checkToProceed(accountNumber.value!!)
        }
    }

    private val _accountStatus = MutableLiveData<AccountStatus?>()
    val accountStatus: LiveData<AccountStatus?>
        get() = _accountStatus

    private fun checkToProceed(accountNo: String) {
        viewModelScope.launch {
            val status = repo.canProceed(accountNo)
            if (status == AccountStatus.PROCEED)
                onNavigateToEnterPin(accountNo)
            _accountStatus.value = status
        }
    }

    fun completeStatus() {
        _accountStatus.value = null
    }

    private fun onNavigateToEnterPin(accountNo: String) {
        _navigateToEnterPin.value = accountNo
    }

    fun onCompleteNavigation() {
        _navigateToEnterPin.value = null
        accountNumber.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}
