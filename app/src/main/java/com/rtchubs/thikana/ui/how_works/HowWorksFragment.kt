package com.rtchubs.thikana.ui.how_works

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.thikana.R
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.databinding.HowWorksBinding
import com.rtchubs.thikana.ui.common.BaseFragment


class HowWorksFragment : BaseFragment<HowWorksBinding, HowWorksViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_how_works
    override val viewModel: HowWorksViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.btnOnboardingStart.setOnClickListener {
        }
    }
}