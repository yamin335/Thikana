package com.rtchubs.thikana.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.Home2Binding
import com.rtchubs.thikana.models.ShoppingMall
import com.rtchubs.thikana.models.ShoppingMallGroup
import com.rtchubs.thikana.ui.LogoutHandlerCallback
import com.rtchubs.thikana.ui.NavDrawerHandlerCallback
import com.rtchubs.thikana.ui.common.BaseFragment
import com.rtchubs.thikana.ui.login.SliderView
import com.rtchubs.thikana.util.RecyclerSectionItemDecoration
import com.rtchubs.thikana.util.showSuccessToast

class Home2Fragment : BaseFragment<Home2Binding, HomeViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main2
    override val viewModel: HomeViewModel by viewModels {
        viewModelFactory
    }

    lateinit var paymentListAdapter: PaymentMethodListAdapter

    private var listener: LogoutHandlerCallback? = null

    private var drawerListener: NavDrawerHandlerCallback? = null

    private var allShoppingMall = ArrayList<ShoppingMallGroup>()

    private lateinit var mallGroupsAdapter: ShoppingMallGroupListAdapter

    private lateinit var mallGroupsSectionItemDecoration: RecyclerSectionItemDecoration

    var mallTypes = arrayOf("all", "mall", "location", "virtual")
    var mallTypeLabels = arrayOf("All Malls", "Shopping Malls", "Location Shops", "Metaverse Malls")

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

        viewDataBinding.btnNewChat.setOnClickListener {
            navigateTo(Home2FragmentDirections.actionHome2FragmentToBotNav())
        }

        viewDataBinding.appLogo.setOnClickListener {
            drawerListener?.toggleNavDrawer()
        }

        viewDataBinding.cartMenu.setOnClickListener {
            navController.navigate(Home2FragmentDirections.actionHome2FragmentToCartNavGraph())
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

        viewModel.slideDataList.forEach { slideData ->
            val slide = SliderView(requireContext())
            slide.sliderTextTitle = slideData.textTitle
            slide.sliderTextDescription = slideData.descText
            slide.sliderImage(slideData.slideImage)
            viewDataBinding.sliderLayout.addSlider(slide)
        }

//        viewDataBinding.pagerMask.setOnClickListener {
//            val position = viewDataBinding.sliderLayout.currentPosition
//            showSuccessToast(requireContext(), "Page selected with position: $position")
//        }

        mallGroupsAdapter = ShoppingMallGroupListAdapter(appExecutors) {
            navController.navigate(Home2FragmentDirections.actionHome2FragmentToAllShopListFragment(it))
        }

        mallGroupsSectionItemDecoration = RecyclerSectionItemDecoration(requireContext()) {
            mallGroupsAdapter.currentList
        }

        viewDataBinding.recyclerMallGroups.addItemDecoration(mallGroupsSectionItemDecoration)

        viewDataBinding.recyclerMallGroups.adapter = mallGroupsAdapter

        val mallsTypeSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, mallTypeLabels)
        mallsTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.spinnerFilter.adapter = mallsTypeSpinnerAdapter

        viewDataBinding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                prepareRecyclerView()
                when(mallTypes[position]) {
                    "all" -> {
                        mallGroupsAdapter.submitList(allShoppingMall)
                    }
                    "mall" -> {
                        mallGroupsAdapter.submitList(allShoppingMall.filter { it.type == "mall" })
                    }
                    "location" -> {
                        mallGroupsAdapter.submitList(allShoppingMall.filter { it.type == "location" })
                    }
                    "virtual" -> {
                        mallGroupsAdapter.submitList(allShoppingMall.filter { it.type == "virtual" })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }

        viewModel.allShoppingMallResponse.observe(viewLifecycleOwner, { response ->
            response?.data?.let { mallList ->
                val shoppingMalls = ArrayList<ShoppingMall>()
                val locationMalls = ArrayList<ShoppingMall>()
                val metaverseMalls = ArrayList<ShoppingMall>()
                if (mallList.isNotEmpty()) {
                    for (mall in mallList) {
                        when (mall.mall_type) {
                            "mall" -> {
                                shoppingMalls.add(mall)
                            }
                            "location" -> {
                                locationMalls.add(mall)
                            }
                            "virtual" -> {
                                metaverseMalls.add(mall)
                            }
                        }
                    }

                    allShoppingMall.clear()
                    allShoppingMall.add(ShoppingMallGroup("Shopping Malls", "mall", shoppingMalls))
                    allShoppingMall.add(ShoppingMallGroup("Location Shops", "location", locationMalls))
                    allShoppingMall.add(ShoppingMallGroup("Metaverse Malls", "virtual", metaverseMalls))
                }
                prepareRecyclerView()
                mallGroupsAdapter.submitList(allShoppingMall)
            }
        })

        viewModel.getAllShoppingMallList()
//
//        Log.e("res", preferencesHelper.getAccessTokenHeader())
//        paymentListAdapter.submitList(viewModel.paymentMethodList)
//        viewDataBinding.recyclerPaymentMethods.adapter = paymentListAdapter
//
//
//
//        paymentListAdapter.onClicked.observe(viewLifecycleOwner, Observer {
//            if (it != null) {
//                if (it.id == "-1") {
//                    /**
//                     * add payment method
//                     */
//                    val action = Home2FragmentDirections.actionHome2FragmentToAddPaymentMethodsFragment()
//                    navController.navigate(action)
//                }
//            }
//        })
    }

    private fun prepareRecyclerView() {
        viewDataBinding.recyclerMallGroups.removeItemDecoration(mallGroupsSectionItemDecoration)
        mallGroupsAdapter = ShoppingMallGroupListAdapter(appExecutors) {
            navController.navigate(Home2FragmentDirections.actionHome2FragmentToAllShopListFragment(it))
        }

        mallGroupsSectionItemDecoration = RecyclerSectionItemDecoration(requireContext()) {
            mallGroupsAdapter.currentList
        }

        viewDataBinding.recyclerMallGroups.addItemDecoration(mallGroupsSectionItemDecoration)

        viewDataBinding.recyclerMallGroups.adapter = mallGroupsAdapter
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.toolbar_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
}