package com.omnyom.yumyum.ui.myinfo

import android.content.Intent
import android.util.Log
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.Auth
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySelectedAllBinding
import com.omnyom.yumyum.helper.GoogleLoginHelper
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.recycler.FeedFragment
import com.omnyom.yumyum.helper.recycler.FeedFragment.Companion.deleteAlert
import com.omnyom.yumyum.helper.recycler.FeedFragment.Companion.goEditFeed
import com.omnyom.yumyum.helper.recycler.FlipFeedPagerAdapter
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.model.feed.Place
import com.omnyom.yumyum.model.feed.PlaceRequest
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import okhttp3.internal.notify


class SelectedAllActivity : BaseBindingActivity<ActivitySelectedAllBinding>(R.layout.activity_selected_all) {
    private val selectedAllVM: SelectedAllViewModel by viewModels()

    override fun extraSetupBinding() {
        binding.apply {
            vm = selectedAllVM
            lifecycleOwner = this@SelectedAllActivity
        }
    }

    override fun setup() {
        selectedAllVM.getData(intent)
    }

    override fun setupViews() {
        binding.viewPagerSelectFeed.apply {
            adapter = FlipFeedPagerAdapter(this@SelectedAllActivity)
            orientation = ViewPager2.ORIENTATION_VERTICAL
            post {
                binding.viewPagerSelectFeed.setCurrentItem(selectedAllVM.position,false)
            }
        }

        binding.btnPopup.setOnClickListener {
            val popupMenu = PopupMenu(this@SelectedAllActivity, it)
            popupMenu.inflate(R.menu.feed_popup)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_feed -> goEditFeed(this)
                    R.id.delete_feed -> deleteAlert(this)
                }
                false
            }
            popupMenu.show()
        }

        supportActionBar?.hide()
    }

    override fun onSubscribe() {
        selectedAllVM.feedData.observe(this) {
            val adapter = binding.viewPagerSelectFeed.adapter as FlipFeedPagerAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun release() { }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        Log.e("RESULT", result?.status.toString())

        if (resultCode == RESULT_OK && requestCode == FeedFragment.EDIT_FEED) {
            selectedAllVM.editFeed(data)
        }
    }


}