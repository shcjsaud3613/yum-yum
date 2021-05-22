package com.omnyom.yumyum

import com.google.gson.GsonBuilder
import com.omnyom.yumyum.helper.LoginJsonAdapter
import com.omnyom.yumyum.model.login.LoginResponse
import com.omnyom.yumyum.interfaces.KakaoApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class kakaoApi {
    companion object {
        const val Base_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 5601d509b34b77820ff92b1e468ece45"
    }
}

object RetrofitBuilder {
    private val client = OkHttpClient.Builder().build()

    val gson = GsonBuilder()
//            .registerTypeAdapter(LoginResponse::class.java, LoginJsonAdapter())
            .create()

    private val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8080/yumyum/")
            .baseUrl("http://k4b206.p.ssafy.io:8081/yumyum/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}

object AiRetrofitBuilder {
    private val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://k4b206.p.ssafy.io:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}


object KakaoRetrofitBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(kakaoApi.Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}