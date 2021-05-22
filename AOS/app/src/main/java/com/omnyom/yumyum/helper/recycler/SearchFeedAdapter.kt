package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.databinding.ListItemSearchFeedBinding
import com.omnyom.yumyum.databinding.ListItemSearchPlaceBinding
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.SingleFeedResponse
import com.omnyom.yumyum.model.search.SearchFeedData
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.eureka.EurekaSingleFeedActivity
import com.omnyom.yumyum.ui.feed.MapActivity
import com.omnyom.yumyum.ui.search.SinglePlaceActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class SearchFeedAdapter(private val activity: Activity) : BaseRecyclerAdapter<SearchFeedAdapter.SearchFeedDataViewHolder, FeedData>() {
    private val _feedData = MutableLiveData<FeedData>().apply {
    }
    val feedData : LiveData<FeedData> = _feedData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFeedDataViewHolder {
        val itemBinding = ListItemSearchFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchFeedDataViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchFeedDataViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            getFeedData(items[position]!!.id.toString().toLong())
        }
    }

    override fun getItemCount(): Int = items.size

    inner class SearchFeedDataViewHolder(private val itemBinding: ListItemSearchFeedBinding) : BaseViewHolder(itemBinding.root) {

        private fun checkPlaceByMap() {
            val feedIntent = Intent(activity, MapActivity::class.java).apply {
                putExtra("feedResult", itemBinding.data as Serializable)
            }
            activity.startActivity(feedIntent)
        }

        override fun bind(model: Any) {
            itemBinding.data = model as? FeedData
        }
    }

    fun getFeedData(feedId : Long) {
        RetrofitManager.retrofitService.getSingleFeeds(feedId, userId).enqueue(object :
            Callback<SingleFeedResponse> {
            override fun onResponse(call: Call<SingleFeedResponse>, response: Response<SingleFeedResponse>) {
                val intent = Intent(activity.application, EurekaSingleFeedActivity::class.java)
                intent.putExtra("feedData", response.body()!!.data)
                activity.startActivity(intent)
            }

            override fun onFailure(call: Call<SingleFeedResponse>, t: Throwable) {
            }
        })
    }
}