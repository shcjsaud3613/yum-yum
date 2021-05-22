package com.omnyom.yumyum.model.like

class LikeRequest (private val feedId: Long, private val userId: Long) {
    fun get() : HashMap<String, Long> {
        return hashMapOf(
                "feedId" to feedId,
                "userId" to userId
        )

    }
}

