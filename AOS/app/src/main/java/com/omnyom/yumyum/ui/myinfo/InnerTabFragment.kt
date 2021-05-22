package com.omnyom.yumyum.ui.myinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.omnyom.yumyum.R

import com.omnyom.yumyum.databinding.FragmentInnerTabBinding
import com.omnyom.yumyum.model.myinfo.PagerAdapters

class InnerTabFragment : Fragment() {

    val binding by lazy { FragmentInnerTabBinding.inflate(layoutInflater) }
    lateinit var tabs: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var pagerAdapters: PagerAdapters

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tabs = binding.tabs
        viewPager = binding.viewPager
        pagerAdapters = PagerAdapters(childFragmentManager)

        pagerAdapters.addFragment(MyFeedFragment(), "My Feed")
        pagerAdapters.addFragment(LikeFeedFragment(), "Liked Feed")

        viewPager.adapter = pagerAdapters

        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.setIcon(R.drawable.ic_dashboard_black_24dp)
        tabs.getTabAt(0)?.text =null
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_heart)
        tabs.getTabAt(1)?.text =null

        return binding.root
    }


}