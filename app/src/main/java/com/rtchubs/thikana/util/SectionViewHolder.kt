package com.rtchubs.thikana.util

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import com.rtchubs.thikana.R

class SectionViewHolder(context: Context) : FrameLayout(context) {
    private var titleTextView: TextView

    init {
        inflate(context, R.layout.list_item_mall_group_section_header, this)
        titleTextView = findViewById(R.id.sectionTitle)
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }
}