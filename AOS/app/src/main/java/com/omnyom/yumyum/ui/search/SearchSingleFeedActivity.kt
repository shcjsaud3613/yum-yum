package com.omnyom.yumyum.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySearchSingleFeedBinding
import com.omnyom.yumyum.helper.*
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.model.search.SearchFeedData
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class SearchSingleFeedActivity : BaseBindingActivity<ActivitySearchSingleFeedBinding>(R.layout.activity_search_single_feed) {
    var feedData : SearchFeedData? = null
    private lateinit var clkRotate : Animation

    override fun extraSetupBinding() {
        feedData = intent.getSerializableExtra("feedData") as SearchFeedData
    }

    override fun setup() {
        clkRotate = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise)
    }
    override fun onPause() {
        binding.searchInnerLayout.foodVideo.pause()
        binding.searchInnerLayout.ivThumbnail.visibility = View.VISIBLE
        super.onPause()
    }

    override fun setupViews() {

    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

    private fun likeFeed(feedId: Long) {
        var call = RetrofitManager.retrofitService.feedLike(LikeRequest(feedId, PreferencesManager.userId).get())
        call.enqueue(object : Callback<LikeResponse> {
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if (response.isSuccessful) {
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                t
            }
        })
    }

    // 안좋아요!
    private fun unlikeFeed(feedId: Long) {
        var call = RetrofitManager.retrofitService.cancelFeedLike(feedId, PreferencesManager.userId)
        call.enqueue(object : Callback<LikeResponse> {
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if (response.isSuccessful) {
                }
            }
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                t
            }

        })
    }

}