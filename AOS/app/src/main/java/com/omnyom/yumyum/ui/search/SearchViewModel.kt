package com.omnyom.yumyum.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.model.search.SearchFeedData
import com.omnyom.yumyum.model.search.SearchFeedListResponse
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.model.search.SearchPlaceListResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private val _recommendedFeeds = MutableLiveData<List<FeedData>>().apply {
        value = arrayListOf()
    }
    val recommendedFeeds : LiveData<List<FeedData>> = _recommendedFeeds

    private val _searchFeedResults = MutableLiveData<List<FeedData>>().apply {
        value = arrayListOf()
    }
    val searchFeedResults : LiveData<List<FeedData>> = _searchFeedResults

    val isSearched = MutableLiveData<Boolean>().apply {
       value = false
    }

    private val _searchPlaceResults = MutableLiveData<List<SearchPlaceData>>().apply {
        value = arrayListOf()
    }
    val searchPlaceResults : LiveData<List<SearchPlaceData>> = _searchPlaceResults

    fun getRecommendedFeeds() {
        var call = retrofitService.getAllRecommendedFeeds(userId)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val list : List<FeedData> = response.body()?.data!!
                    _recommendedFeeds.postValue(list)
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                t
            }
        })
    }

    fun searchFeed(searchText: String) {
        var call = retrofitService.getSearchFeedListByTitle(searchText, userId)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val list : List<FeedData> = response.body()?.data!!.filter { feedData -> feedData.isCompleted }
                    _searchFeedResults.postValue(list)
                }
            }
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                t
            }
        })
    }

    fun searchPlace(searchText: String) {
        var call = retrofitService.getSearchPlaceList("name", searchText)
        call.enqueue(object : Callback<SearchPlaceListResponse> {
            override fun onResponse(call: Call<SearchPlaceListResponse>, response: Response<SearchPlaceListResponse>) {
                if (response.isSuccessful) {
                    val list : List<SearchPlaceData> = response.body()?.data!!
                    _searchPlaceResults.postValue(list)
                }
            }

            override fun onFailure(call: Call<SearchPlaceListResponse>, t: Throwable) {
                t
            }

        })
    }
}