package com.mallzhub.customer.ui.gift_point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.mallzhub.customer.BR
import com.mallzhub.customer.R
import com.mallzhub.customer.databinding.GiftPointHistoryDetailsFragmentBinding
import com.mallzhub.customer.models.GiftPointHistoryDetailsItem
import com.mallzhub.customer.ui.common.BaseFragment

class GiftPointHistoryDetailsFragment : BaseFragment<GiftPointHistoryDetailsFragmentBinding, GiftPointHistoryDetailsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_gift_point_history_details
    override val viewModel: GiftPointHistoryDetailsViewModel by viewModels {
        viewModelFactory
    }

    lateinit var pointHistoryListAdapter: GiftPointsHistoryDetailsListAdapter

    private val args: GiftPointHistoryDetailsFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        if (giftPointHistoryList.isEmpty()) {
            viewModel.getGiftPointHistory()
        } else {
            pointHistoryListAdapter.submitList(giftPointHistoryList)
        }

        visibleGoneEmptyView()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            android.R.id.home -> {
//                navController.navigateUp()
//            }
//        }
//        return true
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        viewDataBinding.toolbar.title = args.title
        viewDataBinding.totalPoints = totalPoints.toString()

        pointHistoryListAdapter = GiftPointsHistoryDetailsListAdapter(appExecutors) {
            //navigateTo(TransactionsFragmentDirections.actionTransactionsFragmentToTransactionDetailsFragment(it))
        }

        viewDataBinding.historyRecycler.adapter = pointHistoryListAdapter

        viewModel.giftPointsHistoryList.observe(viewLifecycleOwner, Observer {
            giftPointHistoryList = it as ArrayList<GiftPointHistoryDetailsItem>
            totalPoints = 0
            for (item in giftPointHistoryList) {
                totalPoints += item.point ?: 0
            }
            viewDataBinding.totalPoints = totalPoints.toString()
            pointHistoryListAdapter.submitList(giftPointHistoryList)
            visibleGoneEmptyView()
        })
    }

    private fun visibleGoneEmptyView() {
        if (giftPointHistoryList.isEmpty()) {
            viewDataBinding.container.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.container.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }

    companion object {
        var giftPointHistoryList = ArrayList<GiftPointHistoryDetailsItem>()
        var totalPoints = 0
    }
}