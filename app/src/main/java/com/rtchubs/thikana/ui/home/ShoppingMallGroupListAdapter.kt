package com.rtchubs.thikana.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.rtchubs.thikana.AppExecutors
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.ListItemMallGroupSectionHeaderBinding
import com.rtchubs.thikana.databinding.ListItemMallGroupsBinding

import com.rtchubs.thikana.models.PaymentMethod
import com.rtchubs.thikana.models.ShoppingMall
import com.rtchubs.thikana.models.ShoppingMallGroup
import com.rtchubs.thikana.util.DataBoundListAdapter

class ShoppingMallGroupListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((ShoppingMall) -> Unit)? = null

) : DataBoundListAdapter<ShoppingMallGroup, ListItemMallGroupsBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<ShoppingMallGroup>() {
        override fun areItemsTheSame(oldItem: ShoppingMallGroup, newItem: ShoppingMallGroup): Boolean {
            return oldItem.type == newItem.type
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ShoppingMallGroup,
            newItem: ShoppingMallGroup
        ): Boolean {
            return oldItem.title == newItem.title
        }

    }) {
    // Properties
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    val onClicked = MutableLiveData<PaymentMethod>()
    override fun createBinding(parent: ViewGroup): ListItemMallGroupsBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_mall_groups, parent, false
        )
    }


    override fun bind(binding: ListItemMallGroupsBinding, position: Int) {
        val item = getItem(position)

        val adapter = ShoppingMallListAdapter(appExecutors, itemCallback)
        binding.recyclerMalls.setHasFixedSize(true)
        binding.recyclerMalls.adapter = adapter
        adapter.submitList(item.malls)
    }
}