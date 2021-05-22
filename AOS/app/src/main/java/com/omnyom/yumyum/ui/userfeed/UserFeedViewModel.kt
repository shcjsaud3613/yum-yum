package com.omnyom.yumyum.ui.userfeed

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.model.userInfo.UserData
import com.omnyom.yumyum.model.userInfo.UserResponse
import com.omnyom.yumyum.ui.feed.FeedCreateActivity
import com.omnyom.yumyum.ui.feed.FeedCreateViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFeedViewModel(application: Application) : AndroidViewModel(application) {
    private var retrofitService: RetrofitService = RetrofitBuilder.buildService(RetrofitService::class.java)

    private val _foodData = MutableLiveData<List<FeedData>>().apply {
        value = ArrayList()
    }
    val foodData : LiveData<List<FeedData>> = _foodData

    private val _authorData = MutableLiveData<UserData>().apply {

    }
    val authorData : LiveData<UserData> = _authorData

    fun getAuthorFeed(authorId:Long) {
        var call = retrofitService.getUserFeeds(authorId, userId)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val list : List<FeedData> = response.body()?.data!!.reversed()
                    _foodData.postValue(list)
                }
            }
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                t
            }

        })
    }

    fun getAuthorData(authorId:Long) {
        var myFeedCall = retrofitService.getUserData(authorId)
        myFeedCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _authorData.postValue(response.body()?.data!!)
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t
            }

        })
    }



}