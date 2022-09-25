package com.rtchubs.thikana.ui.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.thikana.R
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.databinding.RegistrationBinding
import com.rtchubs.thikana.ui.common.BaseFragment

class RegistrationFragment  : BaseFragment<RegistrationBinding, RegistrationViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_registration
    override val viewModel: RegistrationViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.btnGetOtp.setOnClickListener {
        }
    }
}