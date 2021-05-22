package com.omnyom.yumyum.ui.search

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.FragmentSearchBinding
import com.omnyom.yumyum.helper.recycler.AuthorFeedAdapter
import com.omnyom.yumyum.ui.base.BaseBindingFragment
import com.omnyom.yumyum.ui.home.HomeFragment

class SearchFragment : BaseBindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val searchVM: SearchViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback

    override fun extraSetupBinding() { }

    override fun setup() {
        searchVM.getRecommendedFeeds()
    }

    override fun setupViews() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.tvRecommendInfo.visibility = View.GONE
                searchVM.isSearched.value = !query.isNullOrBlank()
                searchVM.searchFeed(query!!)
                searchVM.searchPlace(query!!)
                hidePleaseSearch()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onSubscribe() { }

    override fun release() { }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, HomeFragment())
                transaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun hidePleaseSearch() {
//        binding.ivSearch.visibility = View.GONE
//        binding.tvSearch.visibility = View.GONE
    }
}