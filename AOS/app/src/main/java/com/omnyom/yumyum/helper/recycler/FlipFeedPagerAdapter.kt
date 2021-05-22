package com.omnyom.yumyum.helper.recycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.omnyom.yumyum.model.feed.FeedData

class FlipFeedPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val items: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }

    fun setItems(feedList: List<FeedData>) {
        this.items.clear()
        val tempList = mutableListOf<Fragment>()
        for (feed in feedList) {
            tempList.add(FeedFragment(feed))
        }
        items.addAll(tempList)
    }

    fun addItems(feedList: List<FeedData>) {
        val tempList = mutableListOf<Fragment>()
        for (feed in feedList) {
            tempList.add(FeedFragment(feed))
        }
        items.addAll(tempList)
    }

    fun addItem(feed: FeedData) {
        items.add(FeedFragment(feed))
    }
}