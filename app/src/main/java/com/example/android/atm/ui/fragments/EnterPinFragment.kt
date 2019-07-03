package com.example.android.atm.ui.fragments

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.atm.R
import com.example.android.atm.databinding.EnterPinFragmentBinding
import com.example.android.atm.ui.viewmodels.EnterPinViewModel

class EnterPinFragment : Fragment() {

    private val args: EnterPinFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = EnterPinFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val accountNo = args.accountNo

        val viewModelFactory = EnterPinFragmentViewModelFactory(accountNo, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(EnterPinViewModel::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.passwordStatus.observe(this, Observer {
            if (it != null) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewModel.endDisplay()
            }
        })

        viewModel.passwordTriesMax.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    activity,
                    "your account has been blocked, please contact the bank to unblock",
                    Toast.LENGTH_SHORT
                ).show()
                this.findNavController().popBackStack()
            }
        })

        viewModel.customer.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(EnterPinFragmentDirections.actionEnterPinToWelcome(it.id))
            }
        })
        return binding.root
    }
}

class EnterPinFragmentViewModelFactory(private val accountNo: String, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnterPinViewModel::class.java)) {
            return EnterPinViewModel(accountNo, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
