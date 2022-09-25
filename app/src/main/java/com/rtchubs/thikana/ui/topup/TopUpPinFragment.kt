package com.rtchubs.thikana.ui.topup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.rtchubs.thikana.R
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.databinding.TopUpPinFragmentBinding
import com.rtchubs.thikana.ui.common.BaseFragment
import com.rtchubs.thikana.util.hideKeyboard

class TopUpPinFragment : BaseFragment<TopUpPinFragmentBinding, TopUpPinViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_topup_pin
    override val viewModel: TopUpPinViewModel by viewModels {
        viewModelFactory
    }

    val args: TopUpAmountFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateStatusBarBackgroundColor("#1E4356")
        registerToolbar(viewDataBinding.toolbar)

        val helper = args.topUpHelper

        viewModel.pin.observe(viewLifecycleOwner, Observer {  pin ->
            pin?.let {
                helper.mobile = pin
                viewDataBinding.btnProceed.isEnabled = (it.length == 6)
            }
        })

        viewDataBinding.btnProceed.setOnClickListener {
            hideKeyboard()
            navController.navigate(TopUpPinFragmentDirections.actionTopUpPinFragmentToHome2Fragment())
        }
    }
}