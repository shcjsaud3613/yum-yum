package com.omnyom.yumyum.model.place

data class GetPlaceDataResponse(
    val `data`: PlaceData,
    val message: String,
    val status: String
)

data class PlaceData(
        val address: String,
        val id: Int,
        val locationX: Double,
        val locationY: Double,
        val name: String,
        val phone: String
)