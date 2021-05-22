package com.omnyom.yumyum.ui.myinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.model.userInfo.UserData
import com.omnyom.yumyum.model.userInfo.UserResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoViewModel(application: Application) : BaseViewModel(application) {

    init {
        getUserData()
    }

    private val _userData = MutableLiveData<UserData>().apply { }
    val userData : LiveData<UserData> = _userData

    fun getUserData() {
        retrofitService.getUserData(userId).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _userData.postValue(response.body()?.data!!)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t
            }

        })
    }
}