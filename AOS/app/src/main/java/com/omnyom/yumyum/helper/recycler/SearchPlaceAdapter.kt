package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ListItemSearchPlaceBinding
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.feed.MapActivity
import com.omnyom.yumyum.ui.home.HomeFragment
import com.omnyom.yumyum.ui.search.SinglePlaceActivity
import java.io.Serializable

class SearchPlaceAdapter(private val activity: Activity) : BaseRecyclerAdapter<SearchPlaceAdapter.SearchPlaceDataViewHolder, SearchPlaceData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaceDataViewHolder {
        val itemBinding = ListItemSearchPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchPlaceDataViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchPlaceDataViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(activity.application, SinglePlaceActivity::class.java)
            intent.putExtra("placeData", items[position])
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class SearchPlaceDataViewHolder(private val itemBinding: ListItemSearchPlaceBinding) : BaseViewHolder(itemBinding.root) {
        init {
            itemBinding.btnPlace.setOnClickListener {
                checkPlaceByMap()
            }
        }

        private fun checkPlaceByMap() {
            val placeIntent = Intent(activity, MapActivity::class.java).apply {
                putExtra("placeResult", itemBinding.data as Serializable)
            }
            activity.startActivity(placeIntent)
        }

        override fun bind(model: Any) {
            itemBinding.data = model as? SearchPlaceData
        }
    }
}