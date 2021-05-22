package com.omnyom.yumyum.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.ui.base.BaseViewModel

import retrofit2.*

class HomeViewModel(application: Application) : BaseViewModel(application) {
    // FoodList를 LiveData 객채로 생성
    private val _foodData = MutableLiveData<List<FeedData>>().apply {
    }
    val foodData : LiveData<List<FeedData>> = _foodData

    fun getAllFeeds() {
        var call = retrofitService.getAllFeeds(userId!!)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val feeds = response.body()?.data!!.toMutableList().reversed()
                    val filteredFeeds = feeds.filter {
                            feed -> feed.isCompleted
                    }

                    if (filteredFeeds.count() != _foodData.value?.count()) {
                        _foodData.postValue(filteredFeeds)
                    }
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                t
            }

        })
    }
}

