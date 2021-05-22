package com.omnyom.yumyum.model.feed

data class FeedAiResponse(
    val predictions: List<String>,
    val success: Boolean
)