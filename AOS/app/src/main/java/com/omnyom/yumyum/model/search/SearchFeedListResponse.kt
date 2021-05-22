package com.omnyom.yumyum.model.search

import java.io.Serializable

data class SearchFeedListResponse(
    val `data`: List<SearchFeedData>,
    val message: String,
    val status: String
)

data class SearchFeedData(
        val content: String,
        val id: Int,
        val isLike: Boolean,
        val likeCount: Int,
        val placeId: Int,
        val thumbnailPath: String,
        val title: String,
        val userId: Int,
        val videoPath: String
) : Serializable