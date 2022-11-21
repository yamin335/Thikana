package com.rtchubs.thikana.util

import android.content.Context
import android.graphics.*
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rtchubs.thikana.models.ShoppingMallGroup

class RecyclerSectionItemDecoration(
    private val context: Context,
    private val getItemList: () -> MutableList<ShoppingMallGroup>
): RecyclerView.ItemDecoration() {

    private val dividerHeight = Utils.dpToPx(context, 0.8f)
    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.parseColor("#FF0000")
    }

    private val sectionItemWidth: Int by lazy {
        Utils.getDisplayWidth(context)
    }

    private val sectionItemHeight: Int by lazy {
        Utils.dpToPx(context, 45f)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = parent.layoutManager

        //Just allow linear layout manager
        if (layoutManager !is LinearLayoutManager) {
            return
        }

        // Just allow vertical orientation
        if (LinearLayoutManager.VERTICAL != layoutManager.orientation) {
            return
        }

        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            /* If the item is the first item then a
            section header should be added at the top
             */

            outRect.top = sectionItemHeight
            return
        }

        val currentItem = getItemList()[position]
        val previousItem = getItemList()[position - 1]

        if (currentItem.type != previousItem.type) {
            /* If the group type of the current item is not equal to
             the group type of the previous item then a
             section header should be added at the top of current item
             */
            outRect.top = sectionItemHeight
        } else {
            // Otherwise add a divider between same group items
            outRect.top = dividerHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount

        for(i in 0 until childCount) {
            val childView: View = parent.getChildAt(i)
            val position: Int = parent.getChildAdapterPosition(childView)
            val itemModel = getItemList()[position]
            if (getItemList().isNotEmpty() && (0 == position || itemModel.type != getItemList()[position - 1].type)) {
                val top = childView.top - sectionItemHeight
                drawSectionView(c, itemModel.title, top)
            } else {
                drawDivider(c, childView)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val list = getItemList()
        if (list.isEmpty()) {
            return
        }
        val childCount = parent.childCount
        if (childCount == 0) {
            return
        }
        val firstView = parent.getChildAt(0)
        val position = parent.getChildAdapterPosition(firstView)
        val text = list[position].title
        val itemModel = list[position]

        val condition = if (childCount == 1) true else itemModel.type != list[position + 1].type

        drawSectionView(c, text, if (firstView. bottom <= sectionItemHeight && condition) {
            firstView.bottom - sectionItemHeight
        } else {
            0
        })
    }

    private fun drawDivider(canvas: Canvas, childView: View) {
        canvas.drawRect(
            0f, // Left
            (childView.top - dividerHeight).toFloat(), // Top
            childView.right.toFloat(), // Right
            childView.top.toFloat(), // Bottom
            dividerPaint // divider color
        )
    }

    private fun drawSectionView(canvas: Canvas, text: String, top: Int) {
        val view = SectionViewHolder(context)
        view.setTitle(text)
        val bitmap = getBitmapFromViewGroup(view)
        val bitmapCanvas = Canvas(bitmap)
        view.draw(bitmapCanvas)
        canvas. drawBitmap(bitmap, 0f, top. toFloat(), null)
    }

    private fun getBitmapFromViewGroup(viewGroup: ViewGroup): Bitmap {
        val layoutParams = ViewGroup. LayoutParams(sectionItemWidth, sectionItemHeight)
        viewGroup. layoutParams = layoutParams
        viewGroup.measure(
            View.MeasureSpec.makeMeasureSpec(sectionItemWidth,View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(sectionItemHeight,View.MeasureSpec.EXACTLY)
        )
        viewGroup.layout(0,0, sectionItemWidth, sectionItemHeight)

        val bitmap = Bitmap.createBitmap(viewGroup.width, viewGroup.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        viewGroup.draw(canvas)
        return bitmap
    }
}