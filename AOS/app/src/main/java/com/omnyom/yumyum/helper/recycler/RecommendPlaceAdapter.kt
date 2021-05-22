package com.omnyom.yumyum.helper.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import com.omnyom.yumyum.databinding.ListItemRecommendTitleBinding
import com.omnyom.yumyum.model.feed.PlaceRequest
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.feed.FeedCreateViewModel

class RecommendPlaceAdapter : BaseRecyclerAdapter<RecommendPlaceAdapter.RecommendPlaceViewHolder, SearchPlaceResult>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendPlaceViewHolder {
        val itemBinding = ListItemRecommendTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendPlaceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecommendPlaceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RecommendPlaceViewHolder(private val itemBinding: ListItemRecommendTitleBinding) : BaseViewHolder(itemBinding.root) {
        init {
            itemBinding.btnRecommend.setOnClickListener {
                val feedCreateVM = vm as FeedCreateViewModel
                val searchPlaceResult = itemBinding.placeResult as SearchPlaceResult
                PlaceRequest(
                    searchPlaceResult.address_name,
                    searchPlaceResult.x.toDouble(),
                    searchPlaceResult.y.toDouble(),
                    searchPlaceResult.place_name,
                    searchPlaceResult.phone)?.let {
                    feedCreateVM.placeRequest.postValue(it)
                }
            }
        }

        override fun bind(model: Any) {
            val searchPlaceResult = model as? SearchPlaceResult
            itemBinding.placeResult = searchPlaceResult
            itemBinding.btnRecommend.text = searchPlaceResult?.place_name
        }
    }
}