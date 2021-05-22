package com.omnyom.yumyum.model.feed


data class AllFeedRequest(private val userId: Long) {
    fun get() : HashMap<String, Long> {
        return hashMapOf(
            "userId" to userId
        )
    }
}