package com.omnyom.yumyum.ui.myinfo

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentMyFeedBinding
import com.omnyom.yumyum.helper.recycler.AuthorFeedAdapter
import com.omnyom.yumyum.helper.recycler.MyFeedAdapter
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class MyFeedFragment : BaseBindingFragment<FragmentMyFeedBinding>(R.layout.fragment_my_feed) {
    private val myFeedVM: MyFeedViewModel by viewModels()

    override fun extraSetupBinding() {
        binding.apply {
            vm = myFeedVM
        }
    }

    override fun setup() { }

    override fun setupViews() {
        binding.rvMyFeed.apply {
            adapter = MyFeedAdapter(context, false)
            layoutManager = GridLayoutManager(context, 3)
        }
        binding.btnGoMap.setOnClickListener {
            myFeedVM.goMarkerMap(context as Context)
        }
    }

    override fun onSubscribe() {
        myFeedVM.myFeedData.observe(this) {
            val adapter = binding.rvMyFeed.adapter as MyFeedAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun release() {
    }

    override fun onResume() {
        myFeedVM.getMyFeed()
        super.onResume()
    }
}

