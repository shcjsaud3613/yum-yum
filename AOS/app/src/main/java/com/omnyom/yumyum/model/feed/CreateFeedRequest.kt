package com.omnyom.yumyum.model.feed

import java.io.Serializable


class CreateFeedRequest(
        private val content: String?,
        private val isCompleted: Boolean,
        private val placeRequest: PlaceRequest?,
        private val score : Int?,
        private val thumbnailPath : String?,
        private val title : String?,
        private val userId  : Long,
        private val videoPath : String

) {
    fun get() : HashMap<String, Any?> {
        return hashMapOf(
                "content" to content,
                "isCompleted" to isCompleted,
                "placeRequest" to placeRequest,
                "score" to score,
                "thumbnailPath" to thumbnailPath,
                "title" to title,
                "userId" to userId,
                "videoPath" to videoPath,
        )
    }
}

data class      PlaceRequest(
        var address: String,
        var locationX: Double,
        var locationY: Double,
        var name: String,
        var phone: String
) : Serializable
