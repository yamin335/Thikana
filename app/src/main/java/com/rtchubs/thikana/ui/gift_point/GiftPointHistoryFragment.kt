package com.rtchubs.thikana.ui.gift_point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.GiftPointHistoryFragmentBinding
import com.rtchubs.thikana.models.ShopWiseGiftPointRewards
import com.rtchubs.thikana.ui.common.BaseFragment

class GiftPointHistoryFragment : BaseFragment<GiftPointHistoryFragmentBinding, GiftPointHistoryViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_gift_point_history
    override val viewModel: GiftPointHistoryViewModel by viewModels {
        viewModelFactory
    }

    lateinit var pointHistoryListAdapter: GiftPointsListAdapter

    override fun onResume() {
        super.onResume()
        viewModel.getShopWiseGiftPoints(8)
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

        pointHistoryListAdapter = GiftPointsListAdapter(appExecutors) {
            navigateTo(GiftPointHistoryFragmentDirections.actionGiftPointHistoryFragmentToGiftPointHistoryDetailsFragment(it.shop_name ?: "Unknown Shop", it.merchant_id ?: 0))
        }

        viewDataBinding.historyRecycler.adapter = pointHistoryListAdapter

        viewModel.giftPointsHistoryList.observe(viewLifecycleOwner, Observer {
            giftPointHistoryList = it as ArrayList<ShopWiseGiftPointRewards>
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
        var giftPointHistoryList = ArrayList<ShopWiseGiftPointRewards>()
    }
}