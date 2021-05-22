package com.omnyom.yumyum.ui.eureka

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.net.toUri
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentEurekaBinding
import com.omnyom.yumyum.helper.KakaoMapUtils
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RotateTransformation
import com.omnyom.yumyum.helper.recycler.EurekaAdapter
import com.omnyom.yumyum.model.eureka.Chat
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.skyfishjy.library.RippleBackground
import java.util.*


class EurekaFragment : BaseBindingFragment<FragmentEurekaBinding> (R.layout.fragment_eureka) {
    private val eurekaVM: EurekaViewModel by viewModels()
    val db = Firebase.firestore
    var myFeedId : View? = null
    var myMessageId : View? = null

    override fun extraSetupBinding() {
        binding.apply {
            vm = eurekaVM
        }
    }

    override fun setup() {
        KakaoMapUtils.initLocationFragmentManager(context)
        eurekaVM.getMyFeed(userId)
        eurekaVM.getMyData(userId)

        val chatsRef = db.collection("Chats")
        chatsRef.addSnapshotListener { snapshot, e ->
            eurekaVM.getGeoHashes()
        }

    }

    override fun setupViews() {
        binding.rvEurekaFeedList.apply {
            val _adapter = EurekaAdapter(context).apply {
                setVM(eurekaVM)
            }
            adapter = _adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }

        binding.btnSendMessage.setOnClickListener {
            val message = binding.editTextMessage.text.toString()
            val userId = userId.toString().toInt()
            if (message == "") {
                Toast.makeText(context, "메세지를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                eurekaVM.sendMessage(message, userId)
                binding.eurekaCircleWave.startRippleAnimation()
                Handler().postDelayed({
                    binding.eurekaCircleWave.stopRippleAnimation()
                }, 3000)
            }
            binding.editTextMessage.setText("")
        }

        binding.btnEurekaFeedClose.setOnClickListener {
            binding.clEurekaFeed.visibility = View.GONE
        }
        binding.btnSendFeed.setOnClickListener {
            if (binding.rvEurekaFeedList.visibility == View.VISIBLE) {
                binding.rvEurekaFeedList.visibility = View.GONE
            } else {
                binding.rvEurekaFeedList.visibility = View.VISIBLE
            }

        }

    }

    override fun onSubscribe() {
        eurekaVM.myData.observe(this) {
            Glide.with(this).load(eurekaVM.myData.value!!.profilePath.toUri()).override(100, 100).circleCrop().into(binding.ivEurekaMyProfile)
        }


        eurekaVM.myFeedData.observe(this) {
            val adapter = binding.rvEurekaFeedList.adapter as EurekaAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }

        eurekaVM.matchingDocs.observe(this) {
            val constraintLayout : ConstraintLayout = binding.clMessage
            val docs = eurekaVM.matchingDocs.value!!
            constraintLayout.removeAllViews()
            binding.rvEurekaFeedList.visibility = View.GONE
            for (doc in docs){
                if (doc.feedId == -1) {
                    if (doc.userId == userId.toString().toInt()) {
                        viewMyChat(requireContext(), doc)
                    } else {
                        viewChat(requireContext(), doc)
                    }
                } else {
                    if (doc.userId == userId.toString().toInt()) {
                        viewMyFeed(requireContext(), doc)
                    } else {
                        viewFeed(requireContext(), doc)
                    }

                }
            }
        }

        binding.clMessage.setOnClickListener {
            binding.clEurekaFeed.visibility = View.GONE
        }

        eurekaVM.feedData.observe(this) {
            val feedData = eurekaVM.feedData.value
            binding.tvEurekaFoodName.text = feedData!!.title
            binding.tvEurekaPlace.text = feedData!!.place!!.name + " | "+feedData!!.place!!.address
            binding.tvEurekaContent.text = feedData!!.content
            binding.tvEurekaLikeNum.text = feedData!!.likeCount.toString()
            binding.clEurekaFeed.setOnClickListener {
                val intent = Intent(context, EurekaSingleFeedActivity::class.java)
                intent.putExtra("feedData", feedData)
                startActivity(intent)
            }
        }
    }

    override fun release() {
    }

    fun viewMyChat(context: Context, doc: Chat) {
        val constraintLayout : ConstraintLayout = binding.clEurekaMain
        val message = TextView(context)
        if ( myFeedId != null ) {
            constraintLayout.removeView(myFeedId)
        }
        if ( myMessageId != null ) {
            constraintLayout.removeView(myMessageId)
        }
        message.text = doc.message
        TextViewCompat.setTextAppearance(
                message,
                android.R.style.TextAppearance_DeviceDefault_Large
        )
        message.typeface = Typeface.MONOSPACE
        message.setBackgroundResource(R.drawable.transparent_border_my_chat)
        message.setPadding(
                5.toDp(context),
                3.toDp(context),
                5.toDp(context),
                3.toDp(context)
        )
        message.setTextColor(Color.parseColor("#000000"))
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        message.id = View.generateViewId()
        myMessageId = message
        constraintLayout.addView(message)

        // constraint
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.connect(
                message.id,
                ConstraintSet.START,
                R.id.iv_eureka_my_profile,
                ConstraintSet.START,
                0.toDp(context)
        )
        constraintSet.connect(
                message.id,
                ConstraintSet.BOTTOM,
                R.id.iv_eureka_my_profile,
                ConstraintSet.TOP,
                10.toDp(context)
        )
        constraintSet.connect(
                message.id,
                ConstraintSet.END,
                R.id.iv_eureka_my_profile,
                ConstraintSet.END,
                0.toDp(context)
        )

        constraintSet.applyTo(constraintLayout)
    }

    fun viewMyFeed(context: Context, doc: Chat) {
        val constraintLayout : ConstraintLayout = binding.clEurekaMain
        if ( myFeedId != null ) {
            constraintLayout.removeView(myFeedId)
        }
        if ( myMessageId != null ) {
            constraintLayout.removeView(myMessageId)
        }
        val thumbnail = ImageView(context)
        val layoutParams = ConstraintLayout.LayoutParams(130, 130)
        thumbnail.setLayoutParams(layoutParams)
        thumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).load(doc.thumbnail).transform(RotateTransformation(context, 90f)).into(thumbnail)
        thumbnail.id = View.generateViewId()
        myFeedId = thumbnail
        thumbnail.setPadding(
                3.toDp(context),
                3.toDp(context),
                3.toDp(context),
                3.toDp(context)
        )
        thumbnail.setBackgroundResource(R.drawable.transparent_border)
        thumbnail.setOnClickListener {
            eurekaVM.getFeedData(doc.feedId.toString().toLong(), userId)
            binding.clEurekaFeed.visibility = View.VISIBLE
            Glide.with(context).load(doc.thumbnail).fitCenter().transform(RotateTransformation(context, 90f)).into(binding.ivEurekaThumbnail)
        }
        constraintLayout.addView(thumbnail)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.connect(
                thumbnail.id,
                ConstraintSet.START,
                R.id.iv_eureka_my_profile,
                ConstraintSet.START,
                0.toDp(context)
        )

        constraintSet.connect(
                thumbnail.id,
                ConstraintSet.END,
                R.id.iv_eureka_my_profile,
                ConstraintSet.END,
                0.toDp(context)
        )


        constraintSet.connect(
                thumbnail.id,
                ConstraintSet.BOTTOM,
                R.id.iv_eureka_my_profile,
                ConstraintSet.TOP,
                10.toDp(context)
        )

        constraintSet.applyTo(constraintLayout)
    }


    fun viewChat(context: Context, doc: Chat) {
        val constraintLayout : ConstraintLayout = binding.clMessage
        val random = Random()
        val lng = eurekaVM.positions!![0]
        val lat = eurekaVM.positions!![1]

        val message = TextView(context)
        message.text = doc.message
        TextViewCompat.setTextAppearance(
                message,
                android.R.style.TextAppearance_DeviceDefault_Large
        )
        message.typeface = Typeface.MONOSPACE
        message.setBackgroundResource(R.drawable.transparent_border_chat)
        message.setPadding(
                5.toDp(context),
                3.toDp(context),
                5.toDp(context),
                3.toDp(context)
        )
        message.setTextColor(Color.parseColor("#000000"))
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        message.id = View.generateViewId()
        constraintLayout.addView(message)

        // 유저네임
        val username = TextView(context)
        username.text = doc.nickname
        TextViewCompat.setTextAppearance(
                username,
                android.R.style.TextAppearance_DeviceDefault_Large
        )
        username.typeface = Typeface.MONOSPACE
        username.setTextColor(Color.parseColor("#000000"))
        username.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
        username.id = View.generateViewId()
        constraintLayout.addView(username)

        //프로필
        val profile = ImageView(context)
        Glide.with(context).load(doc.profile).override(100, 100).circleCrop().into(profile)
        profile.id = View.generateViewId()
        constraintLayout.addView(profile)

        // constraint
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        val leftGap = random.nextInt(300) + 30
        val bottomGap = random.nextInt(570) + 50

        if (doc.userId == userId.toString().toInt()) {
            constraintSet.connect(
                    message.id,
                    ConstraintSet.START,
                    R.id.cl_message,
                    ConstraintSet.START,
                    0.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.BOTTOM,
                    R.id.cl_message,
                    ConstraintSet.BOTTOM,
                    0.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.END,
                    R.id.cl_message,
                    ConstraintSet.END,
                    0.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.TOP,
                    R.id.cl_message,
                    ConstraintSet.TOP,
                    0.toDp(context)
            )

        } else {
            constraintSet.connect(
                    message.id,
                    ConstraintSet.START,
                    R.id.cl_message,
                    ConstraintSet.START,
                    leftGap.toDp(context)
            )
            constraintSet.connect(
                    message.id,
                    ConstraintSet.BOTTOM,
                    R.id.cl_message,
                    ConstraintSet.BOTTOM,
                    bottomGap.toDp(context)
            )
        }

        constraintSet.connect(
                profile.id,
                ConstraintSet.START,
                message.id,
                ConstraintSet.START,
                0.toDp(context)
        )

        constraintSet.connect(
                profile.id,
                ConstraintSet.END,
                message.id,
                ConstraintSet.END,
                0.toDp(context)
        )


        constraintSet.connect(
                profile.id,
                ConstraintSet.TOP,
                message.id,
                ConstraintSet.BOTTOM,
                10.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.START,
                message.id,
                ConstraintSet.START,
                0.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.END,
                message.id,
                ConstraintSet.END,
                0.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.TOP,
                profile.id,
                ConstraintSet.BOTTOM,
                10.toDp(context)
        )

        constraintSet.applyTo(constraintLayout)
    }

    fun viewFeed(context: Context, doc: Chat) {
        val constraintLayout : ConstraintLayout = binding.clMessage
        val random = Random()
        val lng = eurekaVM.positions!![0]
        val lat = eurekaVM.positions!![1]


        // 유저네임
        val username = TextView(context)
        username.text = doc.nickname
        TextViewCompat.setTextAppearance(
                username,
                android.R.style.TextAppearance_DeviceDefault_Large
        )
        username.typeface = Typeface.MONOSPACE
        username.setTextColor(Color.parseColor("#000000"))
        username.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)
        username.id = View.generateViewId()
        constraintLayout.addView(username)

        //프로필
        val profile = ImageView(context)
        Glide.with(context).load(doc.profile).override(100, 100).circleCrop().into(profile)
        profile.id = View.generateViewId()
        constraintLayout.addView(profile)

        // 땀네일
        val thumbnail = ImageView(context)
        val layoutParams = ConstraintLayout.LayoutParams(130, 130)
        thumbnail.setLayoutParams(layoutParams)
        thumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(context).load(doc.thumbnail).transform(RotateTransformation(context, 90f)).into(thumbnail)
        thumbnail.id = View.generateViewId()
        thumbnail.setPadding(
            3.toDp(context),
            3.toDp(context),
            3.toDp(context),
            3.toDp(context)
        )
        thumbnail.setBackgroundResource(R.drawable.transparent_border)
        thumbnail.setOnClickListener {
            eurekaVM.getFeedData(doc.feedId.toString().toLong(), userId)
            binding.clEurekaFeed.visibility = View.VISIBLE
            Glide.with(context).load(doc.thumbnail).fitCenter().transform(RotateTransformation(context, 90f)).into(binding.ivEurekaThumbnail)
        }
        constraintLayout.addView(thumbnail)

        // constraint
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        val leftGap = random.nextInt(300) + 30
        val bottomGap = random.nextInt(550) + 50

        if (doc.userId == userId.toString().toInt()) {
            constraintSet.connect(
                    thumbnail.id,
                    ConstraintSet.START,
                    R.id.cl_message,
                    ConstraintSet.START,
                    0.toDp(context)
            )
            constraintSet.connect(
                    thumbnail.id,
                    ConstraintSet.BOTTOM,
                    R.id.cl_message,
                    ConstraintSet.BOTTOM,
                    0.toDp(context)
            )
            constraintSet.connect(
                    thumbnail.id,
                    ConstraintSet.END,
                    R.id.cl_message,
                    ConstraintSet.END,
                    0.toDp(context)
            )
            constraintSet.connect(
                    thumbnail.id,
                    ConstraintSet.TOP,
                    R.id.cl_message,
                    ConstraintSet.TOP,
                    0.toDp(context)
            )

        } else {
            constraintSet.connect(
                    thumbnail.id,
                    ConstraintSet.START,
                    R.id.cl_message,
                    ConstraintSet.START,
                    leftGap.toDp(context)
            )
            constraintSet.connect(
                    thumbnail.id,
                    ConstraintSet.BOTTOM,
                    R.id.cl_message,
                    ConstraintSet.BOTTOM,
                    bottomGap.toDp(context)
            )
        }

        constraintSet.connect(
                profile.id,
                ConstraintSet.START,
                thumbnail.id,
                ConstraintSet.START,
                0.toDp(context)
        )

        constraintSet.connect(
                profile.id,
                ConstraintSet.END,
                thumbnail.id,
                ConstraintSet.END,
                0.toDp(context)
        )


        constraintSet.connect(
                profile.id,
                ConstraintSet.TOP,
                thumbnail.id,
                ConstraintSet.BOTTOM,
                10.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.START,
                thumbnail.id,
                ConstraintSet.START,
                0.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.END,
                thumbnail.id,
                ConstraintSet.END,
                0.toDp(context)
        )

        constraintSet.connect(
                username.id,
                ConstraintSet.TOP,
                profile.id,
                ConstraintSet.BOTTOM,
                10.toDp(context)
        )

        constraintSet.applyTo(constraintLayout)
    }

    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
    ).toInt()

}