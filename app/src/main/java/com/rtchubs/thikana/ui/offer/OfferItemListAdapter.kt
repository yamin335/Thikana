package com.rtchubs.thikana.ui.offer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.rtchubs.thikana.AppExecutors
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.ShopWiseOfferListItemBinding
import com.rtchubs.thikana.models.MerchantWiseOffer
import com.rtchubs.thikana.models.OfferProduct
import com.rtchubs.thikana.util.DataBoundListAdapter

class OfferItemListAdapter(
    private val appExecutors: AppExecutors,
    private val productSelectionCallback: (OfferProduct) -> Unit

) : DataBoundListAdapter<MerchantWiseOffer, ShopWiseOfferListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<MerchantWiseOffer>() {
        override fun areItemsTheSame(oldItem: MerchantWiseOffer, newItem: MerchantWiseOffer): Boolean {
            return oldItem.merchantId == newItem.merchantId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MerchantWiseOffer,
            newItem: MerchantWiseOffer
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun createBinding(parent: ViewGroup): ShopWiseOfferListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_offer, parent, false
        )
    }

    override fun bind(binding: ShopWiseOfferListItemBinding, position: Int) {
        val item = getItem(position)

        val offerProductListAdapter = OfferProductListAdapter(appExecutors) {
            productSelectionCallback.invoke(it)
        }

        binding.rvOfferItems.setHasFixedSize(true)
        binding.rvOfferItems.adapter = offerProductListAdapter
        offerProductListAdapter.submitList(item.offerProductList)
        binding.shopName = item.merchantName
        binding.mallName = "Mall: ${item.mallName}"
        binding.mallLevel = "Level: ${item.mallLevelID}"
    }
}