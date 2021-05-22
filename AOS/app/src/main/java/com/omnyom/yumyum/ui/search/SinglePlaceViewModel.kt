package com.omnyom.yumyum.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SinglePlaceViewModel(application: Application) : BaseViewModel(application) {

    private val _placeFeedData = MutableLiveData<List<FeedData>>().apply {
        value = ArrayList()
    }
    val placeFeedData : LiveData<List<FeedData>> = _placeFeedData

    fun getPlaceFeeds(placeId: Long) {
        var call = retrofitService.getPlaceFeed(placeId, userId)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    _placeFeedData.postValue(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                t
            }

        })
    }
}