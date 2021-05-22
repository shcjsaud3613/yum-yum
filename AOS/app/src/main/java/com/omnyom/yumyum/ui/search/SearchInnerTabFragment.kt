package com.omnyom.yumyum.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentSearchBinding
import com.omnyom.yumyum.databinding.FragmentSearchInnerTabBinding
import com.omnyom.yumyum.helper.recycler.AuthorFeedAdapter
import com.omnyom.yumyum.model.myinfo.PagerAdapters
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class SearchInnerTabFragment : BaseBindingFragment<FragmentSearchInnerTabBinding>(R.layout.fragment_search_inner_tab) {
    private val searchVM: SearchViewModel by viewModels({requireParentFragment()})

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        val tabs = binding.tabsSearch
        val viewPager = binding.vpSearch
        val pagerAdapters = PagerAdapters(childFragmentManager)

        pagerAdapters.addFragment(FoodListFragment(), "음식별")
        pagerAdapters.addFragment(PlaceListFragment(), "식당별")

        viewPager.adapter = pagerAdapters

        tabs.setupWithViewPager(viewPager)

        binding.rvRecommendFeed.apply {
            adapter = AuthorFeedAdapter(context, false)
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    override fun onSubscribe() {
        searchVM.recommendedFeeds.observe(this) {
            val adapter = binding.rvRecommendFeed.adapter as AuthorFeedAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }

        searchVM.isSearched.observe(this) {
            if (it) {
                binding.rvRecommendFeed.visibility = View.INVISIBLE
                binding.searchGroup.visibility = View.VISIBLE
            } else {
                binding.rvRecommendFeed.visibility = View.VISIBLE
                binding.searchGroup.visibility = View.INVISIBLE
            }
        }
    }

    override fun release() { }

}