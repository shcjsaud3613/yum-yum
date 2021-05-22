package com.omnyom.yumyum.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    open fun bind() {}
    open fun bind(data: Any) {}
}