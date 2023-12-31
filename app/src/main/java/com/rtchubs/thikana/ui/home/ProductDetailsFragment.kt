package com.rtchubs.thikana.ui.home

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.rtchubs.thikana.BR
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.ProductDetailsFragmentBinding
import com.rtchubs.thikana.local_db.dao.CartDao
import com.rtchubs.thikana.ui.common.BaseFragment
import com.rtchubs.thikana.util.showSuccessToast
import com.rtchubs.thikana.util.showWarningToast
import kotlinx.android.synthetic.main.list_item_offer_product.*
import javax.inject.Inject

class ProductDetailsFragment :
    BaseFragment<ProductDetailsFragmentBinding, ProductDetailsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_product_details
    override val viewModel: ProductDetailsViewModel by viewModels {
        viewModelFactory
    }

    @Inject
    lateinit var cart: CartDao

    val args: ProductDetailsFragmentArgs by navArgs()

    lateinit var pdImageSampleAdapter: PDImageSampleAdapter
    lateinit var pdColorChooserAdapter: PDColorChooserAdapter
    lateinit var pdSizeChooserAdapter: PDSizeChooserAdapter
    var quantity = 1
    var alreadyAddedToCart = false
    var alreadyAddedToFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        val product = args.product
        val discount = args.discount
        val price = product.mrp ?: 0.0

        viewDataBinding.price = "${getString(R.string.sign_taka)}${price}"

        if (discount > 0) {
            viewDataBinding.discount = discount.toString()
            viewDataBinding.linearOfferPercent.visibility = View.VISIBLE
            viewDataBinding.productPrice.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            viewDataBinding.productDiscountedPrice.visibility = View.VISIBLE

            val discountAmount = (price * discount)/100
            val discountedPrice = price - discountAmount

            product.discount_percent = discount
            product.discountedPrice = discountedPrice

            viewDataBinding.discountedPrice = "${getString(R.string.sign_taka)}${discountedPrice}"
        } else {
            if ((viewDataBinding.productPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                viewDataBinding.productPrice.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }

            viewDataBinding.linearOfferPercent.visibility = View.GONE
            viewDataBinding.productDiscountedPrice.visibility = View.GONE
        }

        viewDataBinding.toolbar.title = product.name
        viewDataBinding.name = product.name

        viewModel.toastWarning.observe(viewLifecycleOwner, Observer {
            it?.let { message ->
                showWarningToast(requireContext(), message)
                viewModel.toastWarning.postValue(null)
            }
        })

        viewModel.toastSuccess.observe(viewLifecycleOwner, Observer {
            it?.let { message ->
                showSuccessToast(requireContext(), message)
                viewModel.toastSuccess.postValue(null)
            }
        })

        pdImageSampleAdapter = PDImageSampleAdapter(
            appExecutors
        ) { item ->
            viewDataBinding.imageUrl = item
        }

        viewDataBinding.rvSampleImage.adapter = pdImageSampleAdapter

        pdImageSampleAdapter.submitList(listOf(product.product_image1, product.product_image2, product.product_image3,
            product.product_image4, product.product_image5))

        viewDataBinding.imageUrl = product.thumbnail
        viewDataBinding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                viewDataBinding.imageView.setImageResource(R.drawable.image_placeholder)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }

        pdColorChooserAdapter = PDColorChooserAdapter(
            appExecutors
        ) { item ->
            //viewDataBinding.imageUrl = item
        }

        viewDataBinding.rvColorChooser.adapter = pdColorChooserAdapter

        pdColorChooserAdapter.submitList(listOf("#d32f2f", "#0AB939", "#2A5D79", "#C7A90D", "#FD87A9", "#E91E63", "#D500F9"))

        //viewDataBinding.rvSizeChooser.addItemDecoration(GridRecyclerItemDecorator(4, 0, true))
        //viewDataBinding.rvSizeChooser.layoutManager = GridLayoutManager(requireContext(), 4)
        pdSizeChooserAdapter = PDSizeChooserAdapter(
            appExecutors
        ) { item ->
            //viewDataBinding.imageUrl = item
        }

        viewDataBinding.rvSizeChooser.adapter = pdSizeChooserAdapter

        pdSizeChooserAdapter.submitList(listOf("XS", "S", "M", "L", "XL", "XXL", "3XL"))

        viewDataBinding.quantity.text = quantity.toString()

        viewDataBinding.decrementQuantity.setOnClickListener {
            if (quantity > 1) --quantity
            viewDataBinding.quantity.text = quantity.toString()
        }

        viewDataBinding.incrementQuantity.setOnClickListener {
            ++quantity
            viewDataBinding.quantity.text = quantity.toString()
        }

        viewDataBinding.addToCart.setOnClickListener {
            if (alreadyAddedToCart) {
                showWarningToast(requireContext(), "Already added to cart!")
            } else {
                viewModel.addToCart(product, quantity)
            }
        }

        viewDataBinding.addToFavorite.setOnClickListener {
            if (alreadyAddedToFavorite) {
                showWarningToast(requireContext(), "Already added to favorite!")
            } else {
                viewModel.addToFavorite(product)
            }
        }

//        viewModel.doesItemExistsInCart(product).observe(viewLifecycleOwner, Observer {
//            it?.let { value ->
//                alreadyAddedToCart = value
//            }
//        })
//
//        viewModel.doesItemExistsInFavorite(product).observe(viewLifecycleOwner, Observer {
//            it?.let { value ->
//                alreadyAddedToFavorite = value
//            }
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_details, menu)

        val menuItem = menu.findItem(R.id.menu_cart)
        val actionView = menuItem.actionView
        val badge = actionView.findViewById<TextView>(R.id.badge)
        badge.text = viewModel.cartItemCount.value?.toString()
        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }

        viewModel.cartItemCount.observe(viewLifecycleOwner, Observer {
            it?.let { value ->
                if (value < 1) {
                    badge.visibility = View.INVISIBLE
                    return@Observer
                } else {
                    badge.visibility = View.VISIBLE
                    badge.text = value.toString()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }

            R.id.menu_cart -> {
                navController.navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartNavGraph2())
            }
        }

        return true
    }
}