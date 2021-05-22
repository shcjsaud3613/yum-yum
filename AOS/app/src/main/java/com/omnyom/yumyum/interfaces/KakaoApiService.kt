package com.omnyom.yumyum.interfaces

import com.omnyom.yumyum.model.maps.SearchKakaoMapResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApiService {
    @GET("/v2/local/search/keyword.json")
    fun placeSearch(
            @Header("Authorization") key: String,
            @Query("query") query: String,
            @Query("x")x: Double,
            @Query("y")y: Double,
            @Query("page")page: Int,
            @Query("size")size: Int,
    ) : Call<SearchKakaoMapResponse>
}