package com.omnyom.yumyum.helper.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.omnyom.yumyum.databinding.ListItemFeedBinding
import com.omnyom.yumyum.helper.RotateTransformation
import com.omnyom.yumyum.model.feed.FeedData
import com.omnyom.yumyum.ui.base.BaseRecyclerAdapter
import com.omnyom.yumyum.ui.base.BaseViewHolder
import com.omnyom.yumyum.ui.myinfo.SelectedAllActivity

class AuthorFeedAdapter(val context: Context, val isLikeFeed: Boolean) : BaseRecyclerAdapter<AuthorFeedAdapter.AuthorFeedViewHolder, FeedData>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : AuthorFeedViewHolder {
        val itemBinding = ListItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorFeedViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AuthorFeedViewHolder, position: Int) {
        holder.bind(items[position])

        Glide.with(context)
            .load(items[position].thumbnailPath)
            .transform(RotateTransformation(context, 90f))
            .into(holder.thumbnail)

        if (!items[position].isCompleted) {
            holder.thumbnail.visibility = View.GONE
        }
        d("여긴어딘가", "$context")

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

    inner class AuthorFeedViewHolder(itemBinding: ListItemFeedBinding) : BaseViewHolder(itemBinding.root) {
        val thumbnail = itemBinding.ivThumbnail
    }

}
