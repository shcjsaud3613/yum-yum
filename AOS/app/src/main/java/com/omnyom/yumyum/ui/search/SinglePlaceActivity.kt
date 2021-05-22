package com.omnyom.yumyum.ui.search

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivitySinglePlaceBinding
import com.omnyom.yumyum.helper.recycler.AuthorFeedAdapter
import com.omnyom.yumyum.model.search.SearchPlaceData
import com.omnyom.yumyum.ui.base.BaseBindingActivity
import com.omnyom.yumyum.ui.feed.MapActivity
import com.omnyom.yumyum.ui.userfeed.UserFeedViewModel
import java.io.Serializable

class SinglePlaceActivity : BaseBindingActivity<ActivitySinglePlaceBinding>(R.layout.activity_single_place) {
    var placeData : SearchPlaceData? = null
    private val singlePlaceVM: SinglePlaceViewModel by viewModels()

    override fun extraSetupBinding() {
    }

    override fun setup() {
        placeData = intent.getSerializableExtra("placeData") as SearchPlaceData
        singlePlaceVM.getPlaceFeeds(placeData!!.id.toString().toLong())

    }

    override fun setupViews() {
        supportActionBar?.hide()
        binding.rvPlaceFeed.apply {
            adapter = AuthorFeedAdapter(this@SinglePlaceActivity, false)
            layoutManager = GridLayoutManager(this@SinglePlaceActivity, 3)
        }

        binding.tvSinglePlaceName.text = placeData!!.name
        binding.tvSinglePlaceAddress.text = placeData!!.address
        binding.tvSinglePlacePhone.text = placeData!!.phone

        binding.tvSinglePlacePhone.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "${placeData!!.phone}")
            startActivity(dialIntent)
        }

        binding.tvSinglePlaceAddress.setOnClickListener {
            val placeIntent = Intent(this, MapActivity::class.java).apply {
                putExtra("placeResult", placeData)
            }
            this.startActivity(placeIntent)
        }
    }

    override fun onSubscribe() {
        singlePlaceVM.placeFeedData.observe(this) {
            val adapter = binding.rvPlaceFeed.adapter as AuthorFeedAdapter
            adapter.run {
                setItems(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun release() {
    }

}