package com.example.android.atm.ui.fragments

import android.app.Activity
import android.app.Application
import android.content.Intent

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.android.atm.R
import com.example.android.atm.databinding.WithdrawFragmentBinding
import com.example.android.atm.ui.dialog.OtherAmountDialog
import com.example.android.atm.ui.dialog.ProgressDialog
import com.example.android.atm.ui.viewmodels.WithdrawViewModel

class WithdrawFragment : Fragment() {

    private val args: WithdrawFragmentArgs by navArgs()

    private lateinit var viewModel: WithdrawViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = WithdrawFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val customerId = args.customerId
        val viewModelFactory = WithdrawFragmentViewModelFactory(customerId, application)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WithdrawViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.status.observe(this, Observer {
            it?.let {
                when (it) {
                    "open_other_amount_dialog" -> {
                        openOtherAmountDialog()
                    }
                    "open_progress_dialog" -> {
                        openProgressDialog()
                    }
                    "cancel" -> this.findNavController().popBackStack()
                    else -> Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                }
                viewModel.endStatus()
            }
        })

        viewModel.trans_id.observe(this, Observer {
            it?.let {
                this.findNavController()
                    .navigate(WithdrawFragmentDirections.actionWithdrawToTransactionSuccessful(it))
                viewModel.cleanTransId()
            }
        })

        return binding.root
    }

    private fun openProgressDialog() {
        val progressDialog = ProgressDialog()
        progressDialog.setTargetFragment(this, REQUEST_PROGRESS_DIALOG_CODE)
        progressDialog.isCancelable = false
        progressDialog.show(fragmentManager, "progress_dialog")
    }

    private fun openOtherAmountDialog() {
        val otherAmountDialog = OtherAmountDialog()
        otherAmountDialog.setTargetFragment(this, REQUEST_OTHER_AMOUNT_DIALOG)
        otherAmountDialog.show(fragmentManager, "other_amount_dialog")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PROGRESS_DIALOG_CODE -> {
                    viewModel.debitCustomer()
                }
                REQUEST_OTHER_AMOUNT_DIALOG -> {
                    val amount = data!!.getStringExtra(OtherAmountDialog.KEY_AMOUNT)
                    viewModel.withdrawAmount(amount)
                }
            }
        }
    }

    companion object {
        const val REQUEST_PROGRESS_DIALOG_CODE = 0
        const val REQUEST_OTHER_AMOUNT_DIALOG = 1
    }

}

class WithdrawFragmentViewModelFactory(private val customerId: Long, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WithdrawViewModel::class.java)) {
            return WithdrawViewModel(customerId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

