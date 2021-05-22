package com.omnyom.yumyum.model.login

data class LoginResponse (val status: String,
                          val error: String?,
                          val code: String?,
                          val message: String,
                          val `data`: LoginData?)

data class LoginData(
        val id : Long,
        val email : String,
        val nickname : String,
        val introduction : String,
        val profilePath : String
)