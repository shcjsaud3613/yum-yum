package com.omnyom.yumyum.ui.useroption

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.PreferencesManager.Companion.userId
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.signup.SignUpRequest
import com.omnyom.yumyum.model.signup.SignUpResponse
import com.omnyom.yumyum.model.signup.UploadProfileResponse
import com.omnyom.yumyum.model.userInfo.UserData
import com.omnyom.yumyum.model.userInfo.UserEditRequest
import com.omnyom.yumyum.model.userInfo.UserResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoEditViewModel(application: Application) : BaseViewModel(application)  {
    private var retrofitService: RetrofitService = RetrofitBuilder.buildService(RetrofitService::class.java)

    init {
        getUserData()
    }

    private val _userData = MutableLiveData<UserData>().apply {

    }
    val userData : LiveData<UserData> = _userData

    val name = MutableLiveData<String>().apply {
        value = ""
    }
    val introduction = MutableLiveData<String>().apply {
        value = ""
    }
    var profilePath = ""

    private val _isComplete: MutableLiveData<Boolean> = MutableLiveData()
    val isComplete: LiveData<Boolean>
        get() = _isComplete

    fun completeEdit() {
        _isComplete.value = true
    }

    fun uploadProfileImage(image: MultipartBody.Part?, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val call = retrofitService.uploadProfile(image)
        call.enqueue(object : Callback<UploadProfileResponse> {
            override fun onResponse(call: Call<UploadProfileResponse>, response: Response<UploadProfileResponse>) {
                if(response.isSuccessful) {
                    Log.d("Result","성공!")
                    profilePath = response.body()!!.data
                    userInfoEdit(userId.toString(), onSuccess, onFailure)
                }
                else {
                    when (response.code()) {
                        404 -> onFailure()
                    }
                }
            }

            override fun onFailure(call: Call<UploadProfileResponse>, t: Throwable) {
                onFailure()
                Log.d("Result","실패!")
            }
        })
    }

    fun userInfoEdit(id: String?, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val call = retrofitService.editUser(
            UserEditRequest(id?:"",
            name.value?:"",
            introduction.value?:"",
            profilePath).get())
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                onSuccess()
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onFailure()
            }

        })
    }

    fun getUserData() {
        var myFeedCall = retrofitService.getUserData(userId)
        myFeedCall.enqueue(object : Callback<UserResponse> {
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