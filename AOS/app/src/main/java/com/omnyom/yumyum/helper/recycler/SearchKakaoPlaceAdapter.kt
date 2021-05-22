package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.omnyom.yumyum.databinding.ListItemKakaoPlaceBinding
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.feed.MapActivity
import com.omnyom.yumyum.ui.feed.SearchPlaceActivity
import java.io.Serializable

class SearchKakaoPlaceAdapter(private val activity: Activity) : BaseRecyclerAdapter<SearchKakaoPlaceAdapter.SearchKakaoPlaceViewHolder, SearchPlaceResult>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKakaoPlaceViewHolder {
        val itemBinding = ListItemKakaoPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchKakaoPlaceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holderKakao: SearchKakaoPlaceViewHolder, position: Int) {
        holderKakao.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SearchKakaoPlaceViewHolder(private val itemBinding: ListItemKakaoPlaceBinding) : BaseViewHolder(itemBinding.root) {
        init {
            itemBinding.root.setOnClickListener {
                if (activity is SearchPlaceActivity) {
                    setPlaceResult()
                }
                Toast.makeText(itemBinding.root.context, "클릭된 아이템 = ${itemBinding.tvPlaceListItemName.text}", Toast.LENGTH_SHORT).show()
            }

            itemBinding.btnPlace.setOnClickListener {
                checkPlaceByMap()
            }
        }

        private fun checkPlaceByMap() {
            val placeIntent = Intent(activity, MapActivity::class.java).apply {
                val place: SearchPlaceResult? = itemBinding.data
                place?.let {
                    val placeData = SearchPlaceData(
                            place.address_name,
                            place.x.toDouble(),
                            place.y.toDouble(),
                            place.place_name,
                            place.phone,
                            -1
                    )
                    putExtra("placeResult", placeData as Serializable)
                }
            }
            activity.startActivity(placeIntent)
        }

        private fun setPlaceResult() {
            val intent = Intent().apply{
                putExtra("placeResult", itemBinding.data as Serializable)
            }
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }

        override fun bind(model: Any) {
            itemBinding.data = model as? SearchPlaceResult
        }
    }
}

