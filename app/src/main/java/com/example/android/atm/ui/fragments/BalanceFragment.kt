package com.example.android.atm.ui.fragments

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.android.atm.R
import com.example.android.atm.databinding.BalanceFragmentBinding
import com.example.android.atm.ui.viewmodels.BalanceViewModel

class BalanceFragment : Fragment() {

    private val args: BalanceFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = BalanceFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val customerId = args.customerId
        val viewModelFactory = BalanceFragmentViewModelFactory(customerId, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(BalanceViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        val adapter = TransactionAdapter()
        binding.recycler.adapter = adapter

        viewModel.transactions.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.status.observe(this, Observer {
            it?.let {
                if (it == "cancel") {
                    this.findNavController().popBackStack()
                }
            }
        })

        return binding.root
    }

}

class BalanceFragmentViewModelFactory(private val customerId: Long, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceViewModel::class.java)) {
            return BalanceViewModel(customerId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

