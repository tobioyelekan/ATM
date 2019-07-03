package com.example.android.atm.ui.dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.atm.databinding.ProgressDialogBinding
import com.example.android.atm.ui.viewmodels.ProcessViewModel

class ProgressDialog : DialogFragment() {
    companion object {
        const val KEY_DONE = "key_done"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = ProgressDialogBinding.inflate(inflater)

        val viewModel = ViewModelProviders.of(this).get(ProcessViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.dialogStatus.observe(this, Observer {
            it?.let {
                when (it) {
                    "done" -> {
                        sendResult()
                    }
                }
                dismiss()
            }
        })

        return binding.root
    }

    private fun sendResult() {
        targetFragment?.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            null
        )
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}