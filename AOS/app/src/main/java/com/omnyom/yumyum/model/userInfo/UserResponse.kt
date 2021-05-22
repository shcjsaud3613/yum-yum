package com.omnyom.yumyum.model.userInfo

data class UserResponse(
    val `data`: UserData,
    val message: String,
    val status: String
)

data class UserData(
        val email: String,
        val id: Int,
        val introduction: String,
        val nickname: String,
        val profilePath: String
)