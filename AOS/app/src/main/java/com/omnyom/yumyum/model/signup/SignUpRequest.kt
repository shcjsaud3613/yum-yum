package com.omnyom.yumyum.model.signup

class SignUpRequest(private val email: String,
                    private val nickname: String,
                    private val introduction: String,
                    private val profilePath: String) {
    fun get() : HashMap<String, String> {
        return hashMapOf(
                "email" to email,
                "nickname" to nickname,
                "introduction" to introduction,
                "profilePath" to profilePath
        )
    }
}
