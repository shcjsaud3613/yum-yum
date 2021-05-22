package com.omnyom.yumyum.ui.feed

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.helper.RetrofitManager.Companion.aiService
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.kakaoApi
import com.omnyom.yumyum.model.feed.*
import com.omnyom.yumyum.model.maps.SearchKakaoMapResponse
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedCreateViewModel(application: Application) : BaseViewModel(application) {
    private var videoPath: String = ""
    private var thumbnailPath: String = ""
    var isCompleted: Boolean = false
    var isEdit: Boolean = false
    var isCalculated: Boolean = false

    val recommendTitles = MutableLiveData<List<String>>().apply {
        value = arrayListOf("    ", "   ", "    ")
    }

    val placeRequest = MutableLiveData<PlaceRequest?>().apply {
        value = PlaceRequest("", 0.0, 0.0, "", "")
    }
    val content = MutableLiveData<String>().apply {
        value = ""
    }
    val id = MutableLiveData<Long>().apply {
        value = 0
    }
    val score = MutableLiveData<Int>().apply {
        value = 0
    }
    val title = MutableLiveData<String>().apply {
        value = ""
    }

    val recommendPlaceResults = MutableLiveData<List<SearchPlaceResult>>().apply {
        value = ArrayList()
    }
    
    // 비디오 데이터를 보냅니다!
    fun feedAi(video: MultipartBody.Part?) {
//        GlobalScope.launch {
//            delay(6000)
//            recommendTitles.postValue(arrayListOf("초밥"))
//        }
        aiService.feedAi(video!!).enqueue(object : Callback<FeedAiResponse> {
            override fun onResponse(call: Call<FeedAiResponse>, response: Response<FeedAiResponse>) {
                isCalculated = true
                if (response.isSuccessful) {
                    recommendTitles.postValue(response.body()?.predictions)
                } else {
                    recommendTitles.postValue(arrayListOf("   ", "   ", "    "))
                }
            }

            override fun onFailure(call: Call<FeedAiResponse>, t: Throwable) {
                isCalculated = true
                recommendTitles.postValue(arrayListOf("   ", "   ", "    "))
            }

        })
    }

    // 비디오 데이터를 보냅니다!
    fun sendVideo(body: MultipartBody.Part?) {
        retrofitService.sendVideo(body!!).enqueue(object : Callback<SendVideoResponse> {
            override fun onResponse(call: Call<SendVideoResponse>, response: Response<SendVideoResponse>) {
                if (response.isSuccessful) {
                    videoPath = response.body()?.data!!.videoPath
                    thumbnailPath = response.body()?.data!!.thumbnailPath
                    createFeed()
                }
            }

            override fun onFailure(call: Call<SendVideoResponse>, t: Throwable) {
                t
            }

        })
    }

    // 응답으로 받은 비디오 데이터를 넣어서 피드 전체 데이터를 보냅니다!
    fun createFeed() {
        val createFeedRequest = CreateFeedRequest(content.value?:"", isCompleted, placeRequest.value, score.value?:0, thumbnailPath, title.value?:"", userId, videoPath)

        retrofitService.createFeed(createFeedRequest.get()).enqueue(object : Callback<CreateFeedResponse> {
            override fun onResponse(call: Call<CreateFeedResponse>, response: Response<CreateFeedResponse>) {
                if (response.isSuccessful) {
                }
            }
            override fun onFailure(call: Call<CreateFeedResponse>, t: Throwable) {
                t
            }

        })
    }

    fun editFeed() {
        val editFeedRequest = EditFeedRequest(content.value?:"", id.value?:-1, isCompleted, placeRequest.value, score.value?:0,  title.value?:"", )

        retrofitService.editFeed(editFeedRequest.get()).enqueue(object : Callback<CreateFeedResponse> {
            override fun onResponse(call: Call<CreateFeedResponse>, response: Response<CreateFeedResponse>) {
                response
            }
            override fun onFailure(call: Call<CreateFeedResponse>, t: Throwable) {
                t
            }
        })
    }

    fun searchPlace(searchText: String) {
        val positions = KakaoMapUtils.getMyPosition(getApplication())
        var call = RetrofitManager.kakaoApiService.placeSearch(kakaoApi.API_KEY, searchText, positions[0], positions[1], 1, 7)
        call.enqueue(object : Callback<SearchKakaoMapResponse> {
            override fun onResponse(call: Call<SearchKakaoMapResponse>, response: Response<SearchKakaoMapResponse>) {
                if (response.isSuccessful) {
                    val list : List<SearchPlaceResult> = response.body()?.documents!!
                    recommendPlaceResults.postValue(list.sortedBy { it.distance.toLong() })
                }
            }

            override fun onFailure(call: Call<SearchKakaoMapResponse>, t: Throwable) {
                t
            }

        })
    }
}