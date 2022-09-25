package com.rtchubs.thikana.ui.offer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.OfferFragmentBinding
import com.rtchubs.thikana.models.MerchantWiseOffer
import com.rtchubs.thikana.ui.LogoutHandlerCallback
import com.rtchubs.thikana.ui.NavDrawerHandlerCallback
import com.rtchubs.thikana.ui.common.BaseFragment

class OfferFragment : BaseFragment<OfferFragmentBinding, OfferViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_offer
    override val viewModel: OfferViewModel by viewModels {
        viewModelFactory
    }

    private var listener: LogoutHandlerCallback? = null

    private var drawerListener: NavDrawerHandlerCallback? = null

    lateinit var offerItemListAdapter: OfferItemListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LogoutHandlerCallback) {
            listener = context
        } else {
            throw RuntimeException("$context must implement LoginHandlerCallback")
        }

        if (context is NavDrawerHandlerCallback) {
            drawerListener = context
        } else {
            throw RuntimeException("$context must implement LoginHandlerCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        drawerListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mActivity.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offerItemListAdapter = OfferItemListAdapter(appExecutors) {
            viewModel.getProductDetails(it.product_id).observe(viewLifecycleOwner, Observer { product ->
                product.merchant = it.merchant
                navigateTo(OfferFragmentDirections.actionOfferFragmentToProductDetailsNavGraph(product, it.discount_percent ?: 0))
            })
        }
        viewDataBinding.rvOfferItems.adapter = offerItemListAdapter

        viewDataBinding.appLogo.setOnClickListener {
            drawerListener?.toggleNavDrawer()
        }

        viewDataBinding.cartMenu.setOnClickListener {
            navController.navigate(OfferFragmentDirections.actionOfferFragmentToCartNavGraph())
        }

        viewModel.cartItemCount.observe(viewLifecycleOwner, Observer {
            it?.let { value ->
                if (value < 1) {
                    viewDataBinding.badge.visibility = View.INVISIBLE
                    return@Observer
                } else {
                    viewDataBinding.badge.visibility = View.VISIBLE
                    viewDataBinding.badge.text = value.toString()
                }
            }
        })

        viewModel.offerProductList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isEmpty()) {
                viewDataBinding.rvOfferItems.visibility = View.GONE
                viewDataBinding.emptyView.visibility = View.VISIBLE
            } else {
                viewDataBinding.rvOfferItems.visibility = View.VISIBLE
                viewDataBinding.emptyView.visibility = View.GONE

                val merchantWiseProductsMap = list.groupBy { it.merchant_id }

                val merchantWiseProductsList: ArrayList<MerchantWiseOffer> = ArrayList()
                for (key in merchantWiseProductsMap.keys) {
                    val productsList = merchantWiseProductsMap[key]
                    val merchantName = if (!productsList.isNullOrEmpty()) {
                        val shopName = productsList[0].merchant?.name
                        if (shopName.isNullOrBlank()) "Unknown Shop" else shopName
                    } else {
                        "Unknown Shop"
                    }

                    if (key != null && !productsList.isNullOrEmpty()) {
                        merchantWiseProductsList.add(MerchantWiseOffer(key, merchantName, productsList))
                    }
                }

                offerItemListAdapter.submitList(merchantWiseProductsList)
            }
        })

        viewModel.getAllOfferList()
    }
}