package com.omnyom.yumyum.ui.myinfo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentLikeFeedBinding
import com.omnyom.yumyum.databinding.FragmentMyInfoBinding
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.useroption.MyOptionActivity


class MyInfoFragment : BaseBindingFragment<FragmentMyInfoBinding>(R.layout.fragment_my_info)  {
    private val myInfoVM: MyInfoViewModel by viewModels()

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        binding.btnGoOptions.setOnClickListener {
            goMyOption()
        }
    }

    override fun onSubscribe() {
        myInfoVM.userData.observe(viewLifecycleOwner, {
            binding.tvUsername.text = it.nickname
            binding.tvIntroduction.text = it.introduction
            Glide.with(this).load(it.profilePath).override(120,120).circleCrop().into(binding.ivAvatar)
        })
    }

    override fun release() { }

    private fun goMyOption() {
        val intent = Intent(context, MyOptionActivity::class.java)
        startActivity(intent)
    }
}