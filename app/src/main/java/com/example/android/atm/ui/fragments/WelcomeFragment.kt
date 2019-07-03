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
import com.example.android.atm.databinding.WelcomeFragmentBinding
import com.example.android.atm.ui.viewmodels.WelcomeViewModel
import com.example.android.atm.ui.viewmodels.WelcomeViewModel.TransactionType

class WelcomeFragment : Fragment() {

    private val args: WelcomeFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = WelcomeFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val customerId = args.customerId
        val viewModelFactory = WelcomeFragmentViewModelFactory(customerId, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(WelcomeViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.status.observe(this, Observer {
            it?.let {
                if (it == "cancel") {
                    this.findNavController().popBackStack()
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                }
                viewModel.endStatus()
            }
        })

        viewModel.getTransactionType.observe(this, Observer {
            it?.let {
                when (it) {
                    TransactionType.TYPE_BALANCE ->
                        this.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeToBalance(customerId))
                    TransactionType.TYPE_WITHDRAW ->
                        this.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeToWithdraw(customerId))
                }
                viewModel.removeTransactionType()
            }
        })

        return binding.root
    }
}

class WelcomeFragmentViewModelFactory(private val customerId: Long, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(customerId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
