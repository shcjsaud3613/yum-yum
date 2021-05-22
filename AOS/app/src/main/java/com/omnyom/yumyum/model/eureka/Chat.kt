package com.omnyom.yumyum.model.eureka

import android.net.Uri

data class Chat(
    val userId: Int,
    val geohash: String,
    val lat: Double,
    val lng: Double,
    val message: String,
    val thumbnail: Uri,
    val feedId: Int,
    val profile: Uri,
    val nickname: String

)