package com.omnyom.yumyum.model.myinfo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val userId : Long,
    val email : String,
    val nickname: String,
    val introduction: String,
    val role: String
)
