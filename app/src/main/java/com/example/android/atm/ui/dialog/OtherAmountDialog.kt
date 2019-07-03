package com.example.android.atm.ui.dialog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.atm.databinding.OtherAmountDialogBinding
import com.example.android.atm.ui.viewmodels.OtherAmountViewModel

class OtherAmountDialog : DialogFragment() {

    private lateinit var viewModel: OtherAmountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = OtherAmountDialogBinding.inflate(inflater)

        viewModel = ViewModelProviders.of(this).get(OtherAmountViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.status.observe(this, Observer {
            it?.let {
                if (it == "done") {
                    sendResult()
                    viewModel.endStatus()
                }
            }
        })

        return binding.root
    }

    private fun sendResult() {

        val intent = Intent()
        intent.putExtra(KEY_AMOUNT, viewModel.amount.value)
        targetFragment?.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            intent
        )
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        const val KEY_AMOUNT = "key_amount"
    }
}