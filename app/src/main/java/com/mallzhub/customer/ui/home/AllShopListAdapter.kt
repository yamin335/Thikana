package com.mallzhub.customer.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mallzhub.customer.AppExecutors
import com.mallzhub.customer.R
import com.mallzhub.customer.databinding.AllShopListItemBinding
import com.mallzhub.customer.models.LevelWiseShops
import com.mallzhub.customer.models.Merchant

import com.mallzhub.customer.models.PaymentMethod
import com.mallzhub.customer.util.DataBoundListAdapter

class AllShopListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((Merchant) -> Unit)? = null
) : DataBoundListAdapter<LevelWiseShops, AllShopListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<LevelWiseShops>() {
        override fun areItemsTheSame(oldItem: LevelWiseShops, newItem: LevelWiseShops): Boolean {
            return oldItem.level?.id == newItem.level?.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: LevelWiseShops,
            newItem: LevelWiseShops
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    // Properties
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    val onClicked = MutableLiveData<PaymentMethod>()
    override fun createBinding(parent: ViewGroup): AllShopListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_all_shops, parent, false
        )
    }


    override fun bind(binding: AllShopListItemBinding, position: Int) {
        val allShopItem = getItem(position)
        binding.level = allShopItem.level?.name

        val shopListAdapter = ShopListAdapter(
            appExecutors
        ) { item ->
            itemCallback?.invoke(item)
        }

        binding.rvShopList.adapter = shopListAdapter

        shopListAdapter.submitList(allShopItem.shops)
    }
}