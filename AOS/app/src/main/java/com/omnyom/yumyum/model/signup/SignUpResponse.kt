package com.omnyom.yumyum.model.signup

data class SignUpResponse(
    val `data`: SignUpData,
    val message: String,
    val status: String
)

data class SignUpData(
        val email: String,
        val id: Long,
        val introduction: String,
        val nickname: String,
        val profilePath: String
)