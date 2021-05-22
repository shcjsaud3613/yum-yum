package com.omnyom.yumyum.model.search

import java.io.Serializable

data class SearchPlaceListResponse(
        val `data`: List<SearchPlaceData>,
        val message: String,
        val status: String
)

data class SearchPlaceData(
        var address: String,
        var locationX: Double,
        var locationY: Double,
        var name: String,
        var phone: String,
        val id: Int
) : Serializable