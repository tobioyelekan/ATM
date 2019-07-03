package com.example.android.atm.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.atm.databinding.InsertCardFragmentBinding
import com.example.android.atm.repos.InsertCardRepo.AccountStatus
import com.example.android.atm.ui.viewmodels.InsertCardViewModel

class InsertCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = InsertCardFragmentBinding.inflate(inflater)

        val viewModel = ViewModelProviders.of(this).get(InsertCardViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.navigateToEnterPin.observe(this, Observer { accountNo ->
            accountNo?.let {
                this.findNavController().navigate(InsertCardFragmentDirections.actionInsertCardToEnterPin(it))
                viewModel.onCompleteNavigation()
            }
        })

        viewModel.accountStatus.observe(this, Observer {
            it?.let {
                if (it == AccountStatus.NOTEXIST) {
                    Toast.makeText(activity, "account does not exist", Toast.LENGTH_SHORT).show()
                } else if (it == AccountStatus.BLOCKED) {
                    Toast.makeText(
                        activity,
                        "account is blocked, please contact your bank to unblock",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.completeStatus()
            }
        })

        return binding.root
    }
}
