package com.omnyom.yumyum.helper.recycler

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.example.messengerapp.Notifications.EurekaData
import com.example.messengerapp.Notifications.Sender
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ListItemFoodBinding
import com.omnyom.yumyum.helper.*
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.interfaces.APISerivce
import com.omnyom.yumyum.model.eureka.Chat
import com.omnyom.yumyum.model.eureka.Client
import com.omnyom.yumyum.model.eureka.EurekaResponse
import com.omnyom.yumyum.model.feed.CreateFeedResponse
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.like.LikeRequest
import com.omnyom.yumyum.model.like.LikeResponse
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.feed.FeedCreateActivity
import com.omnyom.yumyum.ui.userfeed.UserFeedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class FeedFragment(private var feed: FeedData) : BaseBindingFragment<ListItemFoodBinding>(R.layout.list_item_food)  {

    companion object {
        lateinit var curFeed : FeedData

        const val EDIT_FEED = 1234

        fun deleteAlert(activity: Activity) {
            AlertDialog.Builder(ContextThemeWrapper(activity, R.style.Theme_AppCompat_Light_Dialog)).apply {
                setTitle("피드 삭제")
                setMessage("정말 삭제하시겠습니까?")
                setPositiveButton("삭제") { _, _ ->
                    deleteFeed(curFeed.id.toLong())
                    activity.finish()
                }
                setNegativeButton("취소") { _, _ -> }
                show()
            }
        }

        // 피드 삭제
        private fun deleteFeed(feedId: Long) {
            RetrofitManager.retrofitService.deleteFeed(feedId).enqueue(object :
                Callback<CreateFeedResponse> {
                override fun onResponse(call: Call<CreateFeedResponse>, response: Response<CreateFeedResponse>) {
                    Log.d("delete", "$response")

                }
                override fun onFailure(call: Call<CreateFeedResponse>, t: Throwable) {
                    t
                }
            })
        }

        fun goEditFeed(activity: Activity) {
            Intent(activity, FeedCreateActivity::class.java).run {
                putExtra("feedData", curFeed)
                activity.startActivityForResult(this, EDIT_FEED)
            }
        }


    }

    private lateinit var clkRotate : Animation

    private lateinit var expandable : TextView
    private lateinit var btnExpend : ImageButton
    private lateinit var foodVideo : VideoView
    private lateinit var foodName : TextView
    private lateinit var placeName : TextView
    private lateinit var detail : ExpandableTextView
    private lateinit var userName : TextView
    private lateinit var thumbUp : LottieAnimationView
    private lateinit var likeNum : TextView
    private lateinit var ivThumbnail : ImageView
    private lateinit var progressBar : ProgressBar
    private lateinit var btnSend : ImageButton
    private lateinit var avStar : LottieAnimationView

    override fun extraSetupBinding() { }

    override fun setup() {
        binding.feed = feed
        initViewByBinding()
        clkRotate = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clockwise)
    }

    private fun initViewByBinding() {
        expandable = binding.expandableText
        btnExpend = binding.expandCollapse
        foodVideo = binding.foodVideo
        foodName = binding.textName
        placeName = binding.textPlacename
        detail = binding.textDetail
        userName = binding.textUser
        thumbUp = binding.avThumbUp
        likeNum = binding.tvLikeNum
        ivThumbnail = binding.ivThumbnail
        progressBar = binding.progressBar
        avStar = binding.avStar
        btnSend = binding.btnSend
    }

    override fun onResume() {
        initProgressBar()
        initAVStar()
        initTVPlace()
        initTVTitle()
        initTVContent()
        initTVUserName()
        initTVLikeNum()
        curFeed = feed

        super.onResume()
    }

    override fun onPause() {
        foodVideo.pause()
        ivThumbnail.visibility = View.VISIBLE
        super.onPause()
    }

    override fun setupViews() {
        initIsCompleted()
        initIVThumbnail()
        initExpandable()
        initLikeBtn()
        initSendBtn()
        initVideoFood()
    }

    override fun onSubscribe() {
    }

    override fun release() { }

    private fun initProgressBar() {
        if (!foodVideo.isPlaying) {
            progressBar.visibility = View.VISIBLE
            progressBar.startAnimation(clkRotate)
        }
    }

    private fun initAVStar() {
        when(feed.score) {
            0 -> avStar.visibility = View.INVISIBLE
            1 -> avStar.setAnimation(R.raw.ic_vomited)
            2 -> avStar.setAnimation(R.raw.ic_confused)
            3 -> avStar.setAnimation(R.raw.ic_neutral)
            4 -> avStar.setAnimation(R.raw.ic_lol)
            5 -> avStar.setAnimation(R.raw.ic_inloveface)
        }
        avStar.repeatCount = LottieDrawable.INFINITE
        avStar.changeLayersColor(R.color.colorPrimary)
        avStar.playAnimation()
    }

    private fun initIsCompleted() {
        if (!feed.isCompleted) {
            thumbUp.visibility = View.GONE
            likeNum.visibility = View.GONE
        }
    }

    private fun initIVThumbnail() {
        Glide.with(requireActivity())
                .load(feed.thumbnailPath)
                .centerCrop()
                .transform(RotateTransformation(context, 90f))
                .into(ivThumbnail)
    }

    private fun initExpandable() {
        if (expandable.lineCount == 1) {
            btnExpend.visibility = View.GONE
        }
    }

    private fun initTVTitle() {
        if (feed.title == "") {
            foodName.text = "음식명를 입력해주세요"
        } else {
            foodName.text = feed.title
        }
    }

    private fun initTVPlace() {
        if (feed.place == null || feed.place!!.address.isBlank()) {
            placeName.text = "장소를 추가해주세요"
        }
        else {
            placeName.text = feed.place!!.name + " | " + feed.place!!.address
        }
    }

    private fun initTVContent() {
        if (feed.content == "") {
            detail.text = "내용을 입력해주세요"
        } else {
            detail.text = feed.content
        }
    }

    private fun initTVUserName() {
        userName.text = "@" + feed.user.nickname
        userName.setOnClickListener{
            goUserFeed()
        }
    }

    private fun initTVLikeNum() {
        likeNum.text = feed.likeCount.toString()
    }

    private fun initLikeBtn() {
        if (feed.isLike) {
            thumbUp.progress = 0.5f
            thumbUp.changeLayersColor(R.color.colorPrimary)
        } else {
            thumbUp.progress = 0.0f
            thumbUp.changeLayersColor(R.color.white)
        }

        thumbUp.setOnClickListener {
            if (feed.isLike && abs(0.5f - thumbUp.progress) < 0.0005) {
                unlikeFeed(feed.id.toLong())
                feed.likeCount -= 1
                thumbUp.setMinAndMaxProgress(0.5f, 1.0f)
                thumbUp.changeLayersColor(R.color.white)
            } else if (!feed.isLike && (abs(1.0f - thumbUp.progress) < 0.0005 || abs(thumbUp.progress - 0.0f) < 0.0005)) {
                likeFeed(feed.id.toLong())
                feed.likeCount += 1
                thumbUp.setMinAndMaxProgress(0.0f, 0.5f)
                thumbUp.changeLayersColor(R.color.colorPrimary)
            }
            likeNum.text = feed.likeCount.toString()
            feed.isLike = !feed.isLike
            thumbUp.playAnimation()
        }
    }

    private fun initSendBtn() {
        btnSend.setOnClickListener {
            KakaoLinkUtils.kakaoLink(requireActivity(), feed)
        }
    }

    private fun initVideoFood() {
        foodVideo.setVideoURI(feed.videoPath.toUri())

        foodVideo.setOnPreparedListener { mp ->
            foodVideo.start()
            mp.setVolume(0f,0f)
            mp!!.isLooping = true;
            foodVideo.visibility = View.VISIBLE
            progressBar.clearAnimation()
            progressBar.visibility = View.INVISIBLE
            ivThumbnail.visibility = View.INVISIBLE
        }
    }

    private fun goUserFeed() {
        val intent = Intent(context, UserFeedActivity::class.java)
        val authorId = feed.user.id.toString()
        intent.putExtra("authorId", authorId)
        context?.startActivity(intent)
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