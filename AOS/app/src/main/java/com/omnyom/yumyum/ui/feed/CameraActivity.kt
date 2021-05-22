package com.omnyom.yumyum.ui.feed

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityCameraBinding
import com.omnyom.yumyum.ui.base.BaseBindingActivity

class CameraActivity : BaseBindingActivity<ActivityCameraBinding>(R.layout.activity_camera) {
    val PERM_STORAGE = 99 // 외부 저장소 권한 처리
    val PERM_CAMERA = 100 // 카메라 권한처리
    val REQ_CAMERA = 101 // 카메라 촬영 요청

    lateinit var videoUri: String

    override fun extraSetupBinding() { }

    override fun setup() {
        requirePermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)
    }

    override fun setupViews() {
        binding.btnNext.setOnClickListener { startFeedCreateActivity() }
        binding.btnRetry.setOnClickListener { openCamera() }
        supportActionBar?.hide()
    }

    override fun onSubscribe() { }

    override fun release() { }

    fun startFeedCreateActivity() {
        val intent = Intent(this, FeedCreateActivity::class.java).apply {
            putExtra("videoUri", videoUri)
        }
        startActivity(intent)
    }

    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERM_CAMERA -> openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> {
                Toast.makeText(baseContext, "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다!", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA -> {
                Toast.makeText(baseContext, "카메라 권한을 승인해야 카메라를 사용할 수 있습니다!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    fun openCamera() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3)
            also { takeVideoIntent ->
                Toast.makeText(baseContext, "3초간 음식을 맛있게 찍어보세요~", Toast.LENGTH_SHORT).show()
                takeVideoIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takeVideoIntent, REQ_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CAMERA -> {
                    Log.d("captureData", "${data?.data}")
                    videoUri = data?.data.toString()
                    binding.videoPreview.setVideoURI(Uri.parse(videoUri))
                    binding.videoPreview.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                    binding.videoPreview.setOnPreparedListener {mp ->
                        binding.videoPreview.start()
                        mp!!.isLooping = true
                        mp!!.setVolume(0f,0f)
                    }
                }
            }
        } else {
            finish()
        }

    }
}