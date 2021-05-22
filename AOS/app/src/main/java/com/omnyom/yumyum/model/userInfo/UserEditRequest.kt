package com.omnyom.yumyum.model.userInfo

class UserEditRequest(private val id: String,
                    private val nickname: String,
                    private val introduction: String,
                    private val profilePath: String) {
    fun get() : HashMap<String, String> {
        return hashMapOf(
                "id" to id,
                "nickname" to nickname,
                "introduction" to introduction,
                "profilePath" to profilePath
        )
    }
}