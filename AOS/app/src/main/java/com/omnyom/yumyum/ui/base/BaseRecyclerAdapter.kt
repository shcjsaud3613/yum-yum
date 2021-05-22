package com.omnyom.yumyum.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<VH: BaseViewHolder, T : Any> : RecyclerView.Adapter<VH>() {

    protected val items: MutableList<T> = mutableListOf()
    protected var vm : BaseViewModel? = null

    fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
    }

    fun setVM(vm : BaseViewModel){
        this.vm = vm
    }

    fun addItems(items: List<T>) {
        this.items.addAll(items)
    }

    fun addItem(item: T) {
        this.items.add(item)
    }
}