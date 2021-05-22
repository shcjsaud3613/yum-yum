package com.omnyom.yumyum.ui.myinfo

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentLikeFeedBinding
import com.omnyom.yumyum.helper.recycler.AuthorFeedAdapter
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class LikeFeedFragment : BaseBindingFragment<FragmentLikeFeedBinding>(R.layout.fragment_like_feed) {
    private val likeFeedVM: LikeFeedViewModel by viewModels()

    override fun extraSetupBinding() {
        binding.apply {
            vm = likeFeedVM
        }
    }

    override fun setup() { }

    override fun setupViews() {
        binding.rvLikeFeed.apply {
            adapter = AuthorFeedAdapter(context, true)
            layoutManager = GridLayoutManager(context, 3)
        }
        binding.btnGoMap.setOnClickListener {
            likeFeedVM.goMarkerMap(context as Context)
        }
    }

    override fun onSubscribe() {
        likeFeedVM.likeFeedData.observe(this) {
            val adapter = binding.rvLikeFeed.adapter as AuthorFeedAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun release() { }

    override fun onResume() {
        likeFeedVM.getLikeFeed()
        super.onResume()
    }
}
