package com.omnyom.yumyum.ui.eureka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityEurekaSingleFeedBinding
import com.omnyom.yumyum.helper.*
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class EurekaSingleFeedActivity : BaseBindingActivity<ActivityEurekaSingleFeedBinding>(R.layout.activity_eureka_single_feed) {
    var feedData : FeedData? = null
    private lateinit var clkRotate : Animation

    override fun extraSetupBinding() {
        feedData = intent.getSerializableExtra("feedData") as FeedData
    }

    override fun setup() {
        clkRotate = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise)
    }
    override fun onPause() {
        binding.eurekaInnerLayout.foodVideo.pause()
        binding.eurekaInnerLayout.ivThumbnail.visibility = View.VISIBLE
        super.onPause()
    }

    override fun setupViews() {
        supportActionBar?.hide()
        binding.eurekaInnerLayout.progressBar.visibility = View.VISIBLE
        binding.eurekaInnerLayout.progressBar.startAnimation(clkRotate)
        Glide.with(this)
                .load(feedData!!.thumbnailPath)
                .centerCrop()
                .transform(RotateTransformation(this, 90f))
                .into(binding.eurekaInnerLayout.ivThumbnail)
        binding.eurekaInnerLayout.foodVideo.apply {
            setVideoURI(feedData!!.videoPath.toUri())
            setOnPreparedListener { mp ->
                binding.eurekaInnerLayout.foodVideo.start()
                mp.setVolume(0f,0f)
                mp!!.isLooping = true;
                binding.eurekaInnerLayout.foodVideo.visibility = View.VISIBLE
                binding.eurekaInnerLayout.progressBar.clearAnimation()
                binding.eurekaInnerLayout.progressBar.visibility = View.INVISIBLE
                binding.eurekaInnerLayout.ivThumbnail.visibility = View.INVISIBLE
            }
            setOnCompletionListener {
                binding.eurekaInnerLayout.progressBar.clearAnimation()
                binding.eurekaInnerLayout.progressBar.visibility = View.INVISIBLE
                binding.eurekaInnerLayout.ivThumbnail.visibility = View.INVISIBLE
            }
        }

        binding.eurekaInnerLayout.textName.text = feedData!!.title
        binding.eurekaInnerLayout.textDetail.text = feedData!!.content
        binding.eurekaInnerLayout.textPlacename.text = feedData!!.place!!.name +" | "+feedData!!.place!!.address
        binding.eurekaInnerLayout.textUser.text = "@"+feedData!!.user!!.nickname
        binding.eurekaInnerLayout.tvLikeNum.text = feedData!!.likeCount.toString()

        when(feedData!!.score) {
            0 -> binding.eurekaInnerLayout.avStar.visibility = View.INVISIBLE
            1 -> binding.eurekaInnerLayout.avStar.setAnimation(R.raw.ic_vomited)
            2 -> binding.eurekaInnerLayout.avStar.setAnimation(R.raw.ic_confused)
            3 -> binding.eurekaInnerLayout.avStar.setAnimation(R.raw.ic_neutral)
            4 -> binding.eurekaInnerLayout.avStar.setAnimation(R.raw.ic_lol)
            5 -> binding.eurekaInnerLayout.avStar.setAnimation(R.raw.ic_inloveface)
        }
        binding.eurekaInnerLayout.avStar.repeatCount = LottieDrawable.INFINITE
        binding.eurekaInnerLayout.avStar.changeLayersColor(R.color.colorPrimary)
        binding.eurekaInnerLayout.avStar.playAnimation()

        if (feedData!!.isLike) {
            binding.eurekaInnerLayout.avThumbUp.progress = 0.5f
            binding.eurekaInnerLayout.avThumbUp.changeLayersColor(R.color.colorPrimary)
        } else {
            binding.eurekaInnerLayout.avThumbUp.progress = 0.0f
            binding.eurekaInnerLayout.avThumbUp.changeLayersColor(R.color.white)
        }

        binding.eurekaInnerLayout.avThumbUp.setOnClickListener {
            if (feedData!!.isLike && abs(0.5f - binding.eurekaInnerLayout.avThumbUp.progress) < 0.0005) {
                unlikeFeed(feedData!!.id.toLong())
                feedData!!.likeCount -= 1
                binding.eurekaInnerLayout.avThumbUp.setMinAndMaxProgress(0.5f, 1.0f)
                binding.eurekaInnerLayout.avThumbUp.changeLayersColor(R.color.white)
            } else if (!feedData!!.isLike && (abs(1.0f - binding.eurekaInnerLayout.avThumbUp.progress) < 0.0005 || abs(binding.eurekaInnerLayout.avThumbUp.progress - 0.0f) < 0.0005)) {
                likeFeed(feedData!!.id.toLong())
                feedData!!.likeCount += 1
                binding.eurekaInnerLayout.avThumbUp.setMinAndMaxProgress(0.0f, 0.5f)
                binding.eurekaInnerLayout.avThumbUp.changeLayersColor(R.color.colorPrimary)
            }
            binding.eurekaInnerLayout.tvLikeNum.text = feedData!!.likeCount.toString()
            feedData!!.isLike = !feedData!!.isLike
            binding.eurekaInnerLayout.avThumbUp.playAnimation()
        }

        binding.eurekaInnerLayout.btnSend.setOnClickListener {
            KakaoLinkUtils.kakaoLink(this, feedData!!)
        }

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