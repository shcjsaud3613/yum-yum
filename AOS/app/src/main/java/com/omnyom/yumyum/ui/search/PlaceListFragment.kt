package com.omnyom.yumyum.ui.search

import android.app.Activity
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentPlaceListBinding
import com.omnyom.yumyum.helper.recycler.SearchPlaceAdapter
import com.omnyom.yumyum.ui.base.BaseBindingFragment

class PlaceListFragment : BaseBindingFragment<FragmentPlaceListBinding>(R.layout.fragment_place_list) {
    private val searchVM: SearchViewModel by viewModels({requireParentFragment().requireParentFragment()})

    override fun extraSetupBinding() { }

    override fun setup() { }

    override fun setupViews() {
        binding.rvPlace.apply {
            adapter = SearchPlaceAdapter(activity as Activity).apply {
                setVM(searchVM)
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onSubscribe() {
        searchVM.searchPlaceResults.observe(viewLifecycleOwner, {
            val adapter = binding.rvPlace.adapter as SearchPlaceAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
            if (searchVM.isSearched.value == true && it?.count() == 0) {
                showSearchImageView()
            } else {
                hideSearchImageView()
            }
        })
    }

    override fun release() { }

    private fun showSearchImageView() {
        binding.ivSearch.visibility = View.VISIBLE
        binding.tvSearch.visibility = View.VISIBLE
    }

    private fun hideSearchImageView() {
        binding.ivSearch.visibility = View.GONE
        binding.tvSearch.visibility = View.GONE
    }
}