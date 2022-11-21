package com.rtchubs.thikana.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.rtchubs.thikana.AppExecutors
import com.rtchubs.thikana.R
import com.rtchubs.thikana.databinding.ShopListItemBinding
import com.rtchubs.thikana.models.Merchant

import com.rtchubs.thikana.models.PaymentMethod
import com.rtchubs.thikana.util.DataBoundListAdapter

class ShopListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((Merchant) -> Unit)? = null

) : DataBoundListAdapter<Merchant, ShopListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<Merchant>() {
        override fun areItemsTheSame(oldItem: Merchant, newItem: Merchant): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Merchant,
            newItem: Merchant
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    // Properties
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    val onClicked = MutableLiveData<PaymentMethod>()
    override fun createBinding(parent: ViewGroup): ShopListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_shop, parent, false
        )
    }


    override fun bind(binding: ShopListItemBinding, position: Int) {
        val item = getItem(position)
        binding.model = item

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }

        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                binding.logo.setImageResource(R.drawable.shopping_mall)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }
    }
}