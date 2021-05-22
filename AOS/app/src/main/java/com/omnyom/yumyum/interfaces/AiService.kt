package com.omnyom.yumyum.interfaces

import com.omnyom.yumyum.model.feed.FeedAiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AiService {
    @Multipart
    @POST("video/list")
    fun feedAi(@Part video: MultipartBody.Part): Call<FeedAiResponse>
}