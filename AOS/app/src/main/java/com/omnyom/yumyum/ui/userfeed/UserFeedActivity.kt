package com.omnyom.yumyum.ui.userfeed

import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityUserFeedBinding
import com.omnyom.yumyum.helper.recycler.AuthorFeedAdapter
import com.omnyom.yumyum.ui.base.BaseBindingActivity

class UserFeedActivity : BaseBindingActivity<ActivityUserFeedBinding>(R.layout.activity_user_feed) {
    private val userFeedVM: UserFeedViewModel by viewModels()

    override fun extraSetupBinding() {
        binding.apply {
            vm = userFeedVM
            lifecycleOwner = this@UserFeedActivity
        }
    }

    override fun setup() {
        val authorId = intent.getStringExtra("authorId")!!.toLong()
        userFeedVM.getAuthorFeed(authorId)
        userFeedVM.getAuthorData(authorId)
    }

    override fun setupViews() {
        supportActionBar?.hide()
        binding.rvAuthorFeed.apply {
            adapter = AuthorFeedAdapter(this@UserFeedActivity, false)
            layoutManager = GridLayoutManager(this@UserFeedActivity, 3)
        }
    }

    override fun onSubscribe() {
        userFeedVM.foodData.observe(this) {
            val adapter = binding.rvAuthorFeed.adapter as AuthorFeedAdapter
            adapter.run {
                binding.tvAllFeedAuthor.text = "유저피드 (${userFeedVM.foodData.value!!.size})"
                setItems(it)
                notifyDataSetChanged()
            }
        }
        userFeedVM.authorData.observe(this) {
            binding.tvUsername.text = it.nickname
            binding.tvIntroduction.text = it.introduction
            Glide.with(this).load(it.profilePath).circleCrop().into(binding.ivAvatar)
        }
    }

    override fun release() {
    }
}