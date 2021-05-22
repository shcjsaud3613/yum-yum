package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.feed.Place
import com.omnyom.yumyum.ui.base.BaseViewModel
import com.omnyom.yumyum.ui.markermap.MarkerMapActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeFeedViewModel(application: Application) : BaseViewModel(application) {

    private val _likeFeedData = MutableLiveData<List<FeedData>>().apply {
        value = ArrayList()
    }
    val likeFeedData : LiveData<List<FeedData>> = _likeFeedData
    lateinit var placeData : ArrayList<Place>

    fun getLikeFeed() {
        var call = retrofitService.getLikedFeed(userId)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val list : List<FeedData> = response.body()?.data!!.reversed()
                    _likeFeedData.postValue(list)
                    placeData = ArrayList(list.map { item -> item.place ?: Place("", 0, 0.0, 0.0, "", "") })
                }
            }
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                t
            }
        })
    }

    fun goMarkerMap(context: Context) {
        val intent = Intent(context, MarkerMapActivity::class.java).apply {
             putExtra("placeData", placeData)
        }
        context.startActivity(intent)
    }
}