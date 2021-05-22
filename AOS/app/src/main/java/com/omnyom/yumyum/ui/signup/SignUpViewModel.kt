package com.omnyom.yumyum.ui.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omnyom.yumyum.RetrofitBuilder
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.helper.RetrofitManager.Companion.retrofitService
import com.omnyom.yumyum.interfaces.RetrofitService
import com.omnyom.yumyum.model.signup.SignUpRequest
import com.omnyom.yumyum.model.signup.SignUpResponse
import com.omnyom.yumyum.model.signup.UploadProfileResponse
import com.omnyom.yumyum.ui.base.BaseViewModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.*

class SignUpViewModel(application: Application) : BaseViewModel(application) {

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

    fun completeSignUp() {
        _isComplete.value = true
    }

    fun uploadProfileImage(image: MultipartBody.Part?, email: String?, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val call = retrofitService.uploadProfile(image)
        call.enqueue(object : Callback<UploadProfileResponse> {
            override fun onResponse(call: Call<UploadProfileResponse>, response: Response<UploadProfileResponse>) {
                if(response.isSuccessful) {
                    profilePath = response.body()!!.data
                    signUp(email, onSuccess, onFailure)
                }
                else {
                    when (response.code()) {
                        404 -> onFailure()
                    }
                }
            }

            override fun onFailure(call: Call<UploadProfileResponse>, t: Throwable) {
                onFailure()
            }
        })
    }

    fun signUp(email: String?, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (profilePath.isNullOrBlank()) {
            profilePath = "http://k4b206.p.ssafy.io/resources/profile/1620693596780my_profile.jpg"
        }
        val call = retrofitService.signup(SignUpRequest(email?:"",
                name.value?:"",
                introduction.value?:"",
                profilePath).get())
        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                PreferencesManager.setLong(getApplication(), "userId", response.body()?.data!!.id)
                onSuccess()
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onFailure()
            }

        })
    }
}