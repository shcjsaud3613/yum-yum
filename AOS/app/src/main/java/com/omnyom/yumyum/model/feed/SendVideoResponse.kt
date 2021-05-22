package com.omnyom.yumyum.model.feed

data class SendVideoResponse(
    val `data`: SendVideoData,
    val message: String,
    val status: String
)

data class SendVideoData(
        val thumbnailPath: String,
        val videoPath: String
)