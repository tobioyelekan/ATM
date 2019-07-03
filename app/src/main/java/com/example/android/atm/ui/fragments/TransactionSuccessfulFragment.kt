package com.example.android.atm.ui.fragments

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.android.atm.R
import com.example.android.atm.databinding.TransactionSuccessfulFragmentBinding
import com.example.android.atm.ui.viewmodels.TransactionSuccessfulViewModel

class TransactionSuccessfulFragment : Fragment() {

    private val args: TransactionSuccessfulFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = TransactionSuccessfulFragmentBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val transId = args.transId
        val viewModelFactory = TransactionSuccessfulFragmentViewModelFactory(transId, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionSuccessfulViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        binding.cancel.setOnClickListener { this.findNavController().popBackStack() }
        return binding.root

    }
}

class TransactionSuccessfulFragmentViewModelFactory(private val transId: Long, val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionSuccessfulViewModel::class.java)) {
            return TransactionSuccessfulViewModel(transId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


