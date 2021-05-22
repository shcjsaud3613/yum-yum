package com.omnyom.yumyum.helper.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.omnyom.yumyum.databinding.ListItemFeedBinding
import com.omnyom.yumyum.helper.RotateTransformation
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.myinfo.SelectedAllActivity

class MyFeedAdapter(val context: Context, val isLikeFeed: Boolean) : BaseRecyclerAdapter<MyFeedAdapter.MyFeedViewHolder, FeedData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyFeedViewHolder {
        val itemBinding = ListItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFeedViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyFeedViewHolder, position: Int) {
        holder.bind(items[position])

        Glide.with(context)
                .load(items[position].thumbnailPath)
                .transform(RotateTransformation(context, 90f))
                .into(holder.thumbnail)

        if (!items[position].isCompleted) {
            holder.thumbnail.setColorFilter(Color.parseColor("#90ffffff"))
        }

        holder.thumbnail.setOnClickListener {
            val sendData : ArrayList<FeedData> = ArrayList(items.map { item -> item })
            val intent = Intent(context, SelectedAllActivity::class.java)
            intent.putExtra("feedData", sendData)
            intent.putExtra("isLikeFeed", isLikeFeed)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class MyFeedViewHolder(itemBinding: ListItemFeedBinding) : BaseViewHolder(itemBinding.root) {
        val thumbnail = itemBinding.ivThumbnail
    }

}
