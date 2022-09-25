package com.rtchubs.thikana.ui.exams

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.thikana.R
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.databinding.ExamsFragmentBinding
import com.rtchubs.thikana.ui.common.BaseFragment

class ExamsFragment : BaseFragment<ExamsFragmentBinding, ExamsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_exams
    override val viewModel: ExamsViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}