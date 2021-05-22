package com.omnyom.yumyum.ui.feed

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.kakaoApi
import com.omnyom.yumyum.model.maps.SearchKakaoMapResponse
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPlaceViewModel(application: Application) : BaseViewModel(application) {
    private val _searchPlaceResults = MutableLiveData<List<SearchPlaceResult>>().apply {
        value = ArrayList()
    }
    val searchPlaceResults : LiveData<List<SearchPlaceResult>> = _searchPlaceResults

    var isSearched : Boolean = false

    fun searchPlace(searchText: String) {
        val positions = KakaoMapUtils.getMyPosition(getApplication())
        var call = RetrofitManager.kakaoApiService.placeSearch(kakaoApi.API_KEY, searchText, positions[0], positions[1], 1, 7)
        call.enqueue(object : Callback<SearchKakaoMapResponse> {
            override fun onResponse(call: Call<SearchKakaoMapResponse>, response: Response<SearchKakaoMapResponse>) {
                if (response.isSuccessful) {
                    val list : List<SearchPlaceResult> = response.body()?.documents!!
                    _searchPlaceResults.postValue(list)
                }
            }

            override fun onFailure(call: Call<SearchKakaoMapResponse>, t: Throwable) {
                t
            }

        })
    }
}