package com.example.android.atm.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.atm.R
import com.example.android.atm.ui.viewmodels.WithdrawOptionsViewModel

class WithdrawOptions : Fragment() {

    companion object {
        fun newInstance() = WithdrawOptions()
    }

    private lateinit var viewModel: WithdrawOptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.withdraw_options_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WithdrawOptionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
