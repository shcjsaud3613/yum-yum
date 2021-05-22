package com.omnyom.yumyum.ui.feed

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityFeedCreateBinding
import com.omnyom.yumyum.helper.changeLayersColor
import com.omnyom.yumyum.helper.getFileName
import com.omnyom.yumyum.helper.recycler.RecommendPlaceAdapter
import com.omnyom.yumyum.helper.recycler.RecommendTitleAdapter
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.Place
import com.omnyom.yumyum.model.feed.PlaceRequest
import com.omnyom.yumyum.model.maps.SearchPlaceResult
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.Serializable


class FeedCreateActivity : BaseBindingActivity<ActivityFeedCreateBinding>(R.layout.activity_feed_create) {
    private val feedCreateVM: FeedCreateViewModel by viewModels()
    private val avStars: List<LottieAnimationView> by lazy {
        arrayListOf(
                binding.avStar1,
                binding.avStar2,
                binding.avStar3,
                binding.avStar4,
                binding.avStar5
        )
    }
    private val avStarText: List<String> = listOf("우웩", "노맛", "쏘쏘", "마싰따", "쫀맛")

    override fun extraSetupBinding() {
        binding.apply {
            vm = feedCreateVM
            lifecycleOwner = this@FeedCreateActivity
        }
    }

    override fun setup() {
        initFeedDataWhenEdit()
        if (!feedCreateVM.isEdit) {
//            feedCreateVM.feedAi(getVideoFileForAi(intent.getStringExtra("videoUri")!!.toUri()))
            feedCreateVM.feedAi(getMultipartBodyOfVideoForAi(intent.getStringExtra("videoUri")!!.toUri()))
        } else {
            binding.calculatingGroup.visibility = View.INVISIBLE
        }
    }

    override fun setupViews() {
        supportActionBar?.hide()

        binding.btnSubmit.setOnClickListener {
            if (feedCreateVM.isEdit) {
                val intent = Intent().apply {
                    putExtra("id", feedCreateVM.id.value)
                    putExtra("title", feedCreateVM.title.value)
                    putExtra("content", feedCreateVM.content.value)
                    putExtra("score", feedCreateVM.score.value)
                    if (feedCreateVM.placeRequest.value != null) {
                        putExtra("placeRequest", feedCreateVM.placeRequest.value as Serializable)
                    }
                }
                feedCreateVM.editFeed()
                setResult(Activity.RESULT_OK, intent)
            } else {
                feedCreateVM.sendVideo(getMultipartBodyOfVideo(intent.getStringExtra("videoUri")!!.toUri()))
                startMainActivity()
            }

            finish()
        }

        binding.btnGoBack.setOnClickListener { finish() }
        binding.editTextPlace.setOnClickListener { startSearchPlaceActivity() }

        for ((idx, avStar) in avStars.withIndex()) {
            avStar.setOnClickListener {
                avStar.playAnimation()
                feedCreateVM.score.value = idx + 1
                changeAvStarColor(avStar.id)
                changeAvStarText(idx)
                changeAvStarSize(avStar.id)
            }
        }

        binding.rvRecommendTitle.apply {
            adapter = RecommendTitleAdapter().apply {
                setVM(feedCreateVM)
            }
            layoutManager = LinearLayoutManager(this@FeedCreateActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvRecommendPlace.apply {
            adapter = RecommendPlaceAdapter().apply {
                setVM(feedCreateVM)
            }
            layoutManager = LinearLayoutManager(this@FeedCreateActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.avDotsLoading.repeatCount = LottieDrawable.INFINITE
        binding.avDotsLoading.playAnimation()

        textWatcher()
    }

    override fun onSubscribe() {
        feedCreateVM.run {
            for (elem in listOf(score, title, content, placeRequest)) {
                elem.observe(this@FeedCreateActivity, {
                    feedCreateVM.isCompleted = checkAllFeedRequest()
                    if (feedCreateVM.isCompleted) {
                        binding.btnSubmit.text = getString(R.string.complete)
                    } else {
                        binding.btnSubmit.text = getString(R.string.pre_save)
                    }
                })
            }
        }

        feedCreateVM.recommendTitles.observe(this, {
            val adapter = binding.rvRecommendTitle.adapter as RecommendTitleAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
            if (!it.elementAtOrNull(0).isNullOrBlank()) {
                binding.rvRecommendTitle.visibility = View.VISIBLE
                binding.calculatingGroup.visibility = View.INVISIBLE
            } else if (feedCreateVM.isCalculated) {
                binding.rvRecommendTitle.visibility = View.INVISIBLE
                binding.tvCalculatingVideo.visibility = View.VISIBLE
                binding.avDotsLoading.scale = 0.0f
                binding.tvCalculatingVideo.text = "영상을 알아보지 못 하겠어요ㅠ_ㅠ \n 직접 입력해주세요!"
            }
        })

        feedCreateVM.recommendPlaceResults.observe(this, {
            val adapter = binding.rvRecommendPlace.adapter as RecommendPlaceAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        })

        feedCreateVM.placeRequest.observe(this, {
            binding.editTextPlace.setText(it?.name)
        })
    }

    override fun release() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SearchPlaceActivity.PLACE_CODE) {
            if (resultCode == RESULT_OK) {
                val placeResult = data?.getSerializableExtra("placeResult") as SearchPlaceResult
                PlaceRequest(placeResult.address_name, placeResult.x.toDouble(), placeResult.y.toDouble(), placeResult.place_name, placeResult.phone)?.let {
                    feedCreateVM.placeRequest.postValue(it)
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "장소 선택이 취소되었습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 텍스트 변경을 감지합니다.
    private fun textWatcher() {
        binding.tvTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.tvTitle.text!!.isEmpty() || binding.tvTitle.text!!.length > 10) {
                    binding.inputLayoutTitle.error = "올바른 입력값이 아닙니다!"
                } else {
                    binding.inputLayoutTitle.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun startSearchPlaceActivity() {
        val intent = Intent(this, SearchPlaceActivity::class.java)
        startActivityForResult(intent, SearchPlaceActivity.PLACE_CODE)
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(it)
        }
    }

    private fun getVideoFileForAi(videoUri: Uri?): RequestBody? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(videoUri!!, "r", null) ?: return null
        val file = File(cacheDir, contentResolver.getFileName(videoUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        return file.asRequestBody("video/mp4".toMediaTypeOrNull())
//        return MultipartBody.Part.createFormData("video", file.name, requestFile)
    }

    private fun getMultipartBodyOfVideoForAi(videoUri: Uri?): MultipartBody.Part? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(videoUri!!, "r", null) ?: return null
        val file = File(cacheDir, contentResolver.getFileName(videoUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("video", file.name, requestFile)
    }

    private fun getMultipartBodyOfVideo(videoUri: Uri?): MultipartBody.Part? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(videoUri!!, "r", null) ?: return null
        val file = File(cacheDir, contentResolver.getFileName(videoUri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun changeAvStarColor(avStarId: Int) {
        for (avStar in avStars) {
            if (avStar.id == avStarId) {
                avStar.changeLayersColor(R.color.colorPrimary)
            } else {
                avStar.changeLayersColor(R.color.black)
            }
        }
    }

    private fun changeAvStarText(idx: Int) {
        binding.tvStar.text = avStarText[idx]
        binding.tvStar.setTextColor(getColor(R.color.colorPrimary))
    }

    private fun changeAvStarSize(avStarId: Int) {
        for (avStar in avStars) {
            if (avStar.id == avStarId) {
                avStar.loop(true)
                avStar.animate()
                        .scaleX(1.5f)
                        .scaleY(1.5f)
                        .setDuration(200)
                        .withEndAction {  }
            } else {
                avStar.loop(false)
                avStar.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(200)
                        .withEndAction {  }
            }
        }
    }

    private fun checkAllFeedRequest(): Boolean {
        feedCreateVM.run {
            return !(title.value.isNullOrBlank() || content.value.isNullOrBlank() || score.value == 0 || placeRequest.value?.name.isNullOrBlank())
        }
    }

    private fun initFeedDataWhenEdit() {
        if (intent.hasExtra("feedData")) {
            val feedData = intent.getSerializableExtra("feedData") as FeedData
            feedCreateVM.isEdit = true
            binding.tvTitle.text = "피드 수정"
            feedCreateVM.id.postValue(feedData.id.toLong())
            feedCreateVM.title.postValue(feedData.title)
            feedCreateVM.content.postValue(feedData.content)
            feedCreateVM.score.postValue(feedData.score)

            if (feedData.place != null) {
                val feedPlace = feedData.place ?: Place("", 0, 0.0, 0.0, "", "")

                PlaceRequest(
                        feedPlace.address,
                        feedPlace.locationX,
                        feedPlace.locationY,
                        feedPlace.name,
                        feedPlace.phone
                ).let {
                    feedCreateVM.placeRequest.postValue(it)
//                    binding.editTextPlace.setText(it.name)
                }
            }

            if (feedData.score != 0) {
                changeAvStarColor(avStars[feedData.score - 1].id)
                changeAvStarText(feedData.score - 1)
                changeAvStarSize(avStars[feedData.score - 1].id)
            }
        }
    }
}



