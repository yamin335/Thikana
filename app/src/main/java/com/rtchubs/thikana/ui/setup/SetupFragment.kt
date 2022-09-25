package com.rtchubs.thikana.ui.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.thikana.R
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.databinding.SetupBinding
import com.rtchubs.thikana.ui.common.BaseFragment

class SetupFragment  : BaseFragment<SetupBinding, SetupViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_setup
    override val viewModel: SetupViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.btnProceed.setOnClickListener {
        }

    }
}