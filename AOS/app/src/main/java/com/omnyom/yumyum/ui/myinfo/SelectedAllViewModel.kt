package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager
import com.omnyom.yumyum.helper.notifyObserver
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.FeedResponse
import com.omnyom.yumyum.model.feed.Place
import com.omnyom.yumyum.model.feed.PlaceRequest
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectedAllViewModel(application: Application) : BaseViewModel(application) {
    private val _feedData = MutableLiveData<List<FeedData>>().apply {
    }
    val feedData : LiveData<List<FeedData>> = _feedData

    var isLikeFeed : Boolean = false

    var position : Int = 0

    fun getData(intent: Intent) {
        val feedData = intent.getSerializableExtra("feedData") as ArrayList<FeedData>
        position = intent.getIntExtra("position", 0)
        isLikeFeed = intent.getBooleanExtra("isLikeFeed", false)
        _feedData.postValue(feedData)
    }

    fun editFeed(data: Intent?) {
        var editFeed = findEditFeed(data?.getLongExtra("id", -1) ?: -1)
        editFeed?.let {
            it.title = data?.getStringExtra("title") ?: ""
            it.content = data?.getStringExtra("content") ?: ""
            it.score = data?.getIntExtra("score", -1) ?: -1
            if (data?.hasExtra("placeRequest")!!) {
                val placeRequest = data?.getSerializableExtra("placeRequest") as PlaceRequest
                it.place = Place(
                        placeRequest.address,
                        0,
                        placeRequest.locationX,
                        placeRequest.locationY,
                        placeRequest.name,
                        placeRequest.phone
                )
            }
        }
        _feedData.notifyObserver()
    }

    private fun findEditFeed(id: Long): FeedData? {
        return _feedData.value?.find { feed -> feed.id.toLong() == id }
    }
}
