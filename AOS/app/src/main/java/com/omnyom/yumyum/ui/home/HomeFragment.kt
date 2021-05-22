package com.omnyom.yumyum.ui.home

import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentHomeBinding
import com.omnyom.yumyum.helper.recycler.FeedFragment.Companion.deleteAlert
import com.omnyom.yumyum.helper.recycler.FeedFragment.Companion.goEditFeed
import com.omnyom.yumyum.helper.recycler.FlipFeedPagerAdapter
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeVM: HomeViewModel by viewModels()

    override fun extraSetupBinding() { }

    override fun setup() {  }

    override fun setupViews() {
        binding.viewPager.run {
            orientation = ViewPager2.ORIENTATION_VERTICAL
            adapter = FlipFeedPagerAdapter(requireActivity())
        }
        binding.btnPopup.visibility = View.GONE

        binding.btnPopup.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.inflate(R.menu.feed_popup)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_feed -> goEditFeed(requireActivity())
                    R.id.delete_feed -> deleteAlert(requireActivity())
                }
                false
            }
            popupMenu.show()
        }
    }

    override fun onSubscribe() {
        homeVM.foodData.observe(viewLifecycleOwner, {
            val adapter = binding.viewPager.adapter as FlipFeedPagerAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        })
    }

    override fun release() { }

    override fun onResume() {
        homeVM.getAllFeeds()
        super.onResume()
    }
}