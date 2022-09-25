package com.rtchubs.thikana.ui.tou

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.thikana.R
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.databinding.TouBinding
import com.rtchubs.thikana.ui.common.BaseFragment

class TouFragment  : BaseFragment<TouBinding, TouViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_tou
    override val viewModel: TouViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.btnAgree.setOnClickListener {
        }
    }
}